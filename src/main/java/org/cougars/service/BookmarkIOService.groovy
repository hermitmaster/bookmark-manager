package org.cougars.service

import groovy.util.logging.Slf4j
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.cougars.bean.BookmarkBean
import org.cougars.domain.Bookmark
import org.cougars.domain.BookmarkCategory
import org.cougars.domain.Status
import org.cougars.domain.User
import org.cougars.repository.BookmarkCategoryRepository
import org.cougars.repository.BookmarkRepository
import org.cougars.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/** A service class for importing and exporting bookmark and category data.
 * Created by Dennis Rausch on 11/2/16.
 */

@Slf4j
@Service
class BookmarkIOService {
    @Autowired private UserRepository ur
    @Autowired private BookmarkRepository br
    @Autowired private BookmarkCategoryRepository bcr
    @Autowired private BookmarkValidatorService bvs

    /** Import bookmarks from an Excel file
     *
     * @param file      the Excel file to import bookmarks from
     * @param replace   replace current bookmarks if true, else add to table
     */
    void importBookmarks(MultipartFile file, boolean replace) {
        log.info("Beginning bookmark import. This may take some time.")

        try {
            Set<BookmarkBean> beans = extractBookmarksFromExcel(file)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
            User user = ur.findByUsername(authentication.getName())
            Set<Bookmark> bookmarks = new HashSet<>()

            if(replace && beans) {
                br.deleteInBatch(br.findAll())
                br.flush()
            }

            beans.each {
                BookmarkCategory category = bcr.findByName(it.bookmarkCategory.trim())
                if(!category) {
                    category = new BookmarkCategory(it.bookmarkCategory.trim(), bcr.findByName("None"), user)
                }

                BookmarkCategory subcategory = bcr.findByName("None")
                if(it.subcategory && !it.subcategory.equalsIgnoreCase("None")) {
                    subcategory = bcr.findByName(it.subcategory.trim()) ?: new BookmarkCategory(it.subcategory.trim(), category, user)
                }

                Bookmark bookmark = new Bookmark(it.url.trim(), it.name.trim(), it.description, category, subcategory, user)
                if(user?.isAdmin()) {
                    bookmark.status = Status.ACTIVE
                }

                br.save(bookmark)
                bookmarks.add(bookmark)
            }

            br.save(bookmarks)

            // Validate asynchronously. It can take a long time.
            Thread.start {
                bvs.validateBookmarks(bookmarks)
            }

            log.info("Bookmark import completed.")
        } catch (IOException e) {
            log.error("Unable to import bookmarks!", e)
        }
    }

    /** Extract bookmark data from the import file
     *
     * @param file  the Excel file to extract bookmarks from
     * @return      A set of bookmark beans to ensure uniqueness
     */
    Set<BookmarkBean> extractBookmarksFromExcel(MultipartFile file) {
        Set<BookmarkBean> beans = new HashSet<>()

        try {
            Workbook workbook = getWorkbook(file.inputStream, file.originalFilename)
            Sheet sheet = workbook.getSheet("bookmarks")

            br.deleteInBatch(br.findAll())
            br.flush()

            sheet.each { Row row ->
                BookmarkBean bean = new BookmarkBean()

                row.each { Cell cell ->
                    switch (cell.columnIndex) {
                        case 0:
                            bean.url = cell.stringCellValue.trim()
                            break
                        case 1:
                            bean.name = cell.stringCellValue.trim()
                            break
                        case 2:
                            bean.description = cell.stringCellValue.trim()
                            break
                        case 3:
                            bean.bookmarkCategory = cell.stringCellValue.trim()
                            break
                        case 4:
                            bean.subcategory = cell.stringCellValue.trim()
                            break
                    }
                }

                beans.add(bean)
            }

            log.info("Bookmark extraction completed.")
        } catch (IOException e) {
            log.error("Unable to extract bookmarks!", e)
        }

        return beans
    }

    /** Export bookmarks from the database and write a .xlsx file to an OutputStream
     *
     * @param outputStream  The stream to write the output to
     */
    void exportBookmarks(OutputStream outputStream) {
        log.info("Beginning bookmark export. This may take some time.")
        Set<Bookmark> bookmarks = br.findAll()
        Workbook workbook = new XSSFWorkbook()
        XSSFSheet bookmarkSheet = workbook.createSheet("bookmarks")

        try {
            bookmarks.eachWithIndex { bookmark, idx ->
                XSSFRow row = bookmarkSheet.createRow(idx)
                row.createCell(0).setCellValue(bookmark?.url)
                row.createCell(1).setCellValue(bookmark?.name)
                row.createCell(2).setCellValue(bookmark?.description)
                row.createCell(3).setCellValue(bookmark?.bookmarkCategory?.name)
                row.createCell(4).setCellValue(bookmark?.subcategory?.name)
            }

            workbook.write(outputStream)
            log.info("Bookmark export completed.")
        } catch (IOException e) {
            log.error("Error exporting bookmarks!", e)
        }
    }

    /** Create a new Workbook from an InputStream base on the Excel file format
     *
     * @param inputStream       The InputStream to build the Workbook from
     * @param fileName          The name of the file associated with the InputStream
     * @return                  A Workbook of the appropriate format, given the file type
     * @throws IOException      if file type is not an Excel format
     */
    private static Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        Workbook workbook

        if (fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream)
        } else if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream)
        } else {
            throw new IllegalArgumentException("The specified file is not an Excel file!")
        }

        return workbook
    }
}
