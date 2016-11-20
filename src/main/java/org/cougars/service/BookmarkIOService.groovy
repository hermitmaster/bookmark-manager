/**
 MIT License

 Copyright (c) 2016 MetroState-Cougars

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

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
                            bookmark.url = cell.stringCellValue.trim()
                            break
                        case 1:
                            bookmark.name = cell.stringCellValue.trim()
                            break
                        case 2:
                            bookmark.description = cell.stringCellValue.trim()
                            break
                        case 3:
                            String categoryName = cell.stringCellValue.trim()
                            bookmarkCategory = bcr.findByName(categoryName)
                            if(!bookmarkCategory) {
                                bookmarkCategory = new BookmarkCategory(categoryName, bcr.findByName("None"), user)
                                bcr.save(bookmarkCategory)
                            }
                            bookmark.bookmarkCategory = bookmarkCategory
                            break
                        case 4:
                            String categoryName = cell.stringCellValue.trim()
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
                bvs.validateBookmarks(bookmarks)
            }

            log.info("Bookmark import completed.")
        } catch (IOException e) {
            log.error("Unable to import bookmarks!", e)
        }
    }

    /** Export bookmarks from the database and write to an OutputStream
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
            throw new IllegalArgumentException("The specified file is not Excel file")
        }

        return workbook
    }
}
