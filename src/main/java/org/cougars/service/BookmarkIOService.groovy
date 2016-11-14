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

/**
 * Created by Dennis Rausch on 11/2/16.
 */

@Slf4j
@Service
class BookmarkIOService {
    @Autowired private UserRepository ur
    @Autowired private BookmarkRepository br
    @Autowired private BookmarkCategoryRepository bcr
    @Autowired private BookmarkValidatorService bvs

    void importBookmarks(MultipartFile file) {
        log.info("Beginning bookmark import. This may take some time.")

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
            User user = ur.findByUsername(authentication.getName())
            Workbook workbook = getWorkbook(file.inputStream, file.originalFilename)
            Sheet sheet = workbook.getSheet("bookmarks")

            br.deleteInBatch(br.findAll())
            br.flush()
            Set<Bookmark> bookmarks = new HashSet<>()

            sheet.each { Row row ->
                Bookmark bookmark = new Bookmark()
                bookmark.status = Status.ACTIVE
                bookmark.createdBy = user

                row.each { Cell cell ->
                    BookmarkCategory bookmarkCategory = null

                    switch (cell.columnIndex) {
                        case 0:
                            bookmark.url = cell.stringCellValue
                            break
                        case 1:
                            bookmark.name = cell.stringCellValue
                            break
                        case 2:
                            bookmark.description = cell.stringCellValue
                            break
                        case 3:
                            String categoryName = cell.stringCellValue
                            bookmarkCategory = bcr.findByName(categoryName)
                            if(!bookmarkCategory) {
                                bookmarkCategory = new BookmarkCategory(categoryName, bcr.findByName("None"), user)
                                bcr.save(bookmarkCategory)
                            }
                            bookmark.bookmarkCategory = bookmarkCategory
                            break
                        case 4:
                            String categoryName = cell.stringCellValue
                            BookmarkCategory subcategory = bcr.findByName(categoryName)
                            if(!subcategory) {
                                subcategory = new BookmarkCategory(categoryName, bookmarkCategory, user)
                                bcr.save(subcategory)
                            }
                            bookmark.subcategory = bcr.findByName(subcategory.name)
                            break
                    }
                }

                bookmarks.add(bookmark)
            }

            br.save(bookmarks)

            Thread.start {
                bvs.validateBookmarks()
            }

            log.info("Bookmark import completed.")
        } catch (IOException e) {
            log.error("Unable to import bookmarks!", e)
        }
    }

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

    private static Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        Workbook workbook

        if (fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream)
        } else if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream)
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file")
        }

        return workbook
    }
}
