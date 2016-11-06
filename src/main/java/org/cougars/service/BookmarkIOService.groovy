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
import org.cougars.repository.BookmarkRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/**
 * Created by Dennis Rausch on 11/2/16.
 */

@Slf4j
@Service
class BookmarkIOService {
    @Autowired
    BookmarkRepository bookmarkRepository

    void importBookmarks(MultipartFile file) {
        log.info("Beginning bookmark import. This may take some time.")
        Set<Bookmark> bookmarks = new HashSet<>()
        Workbook workbook = getWorkbook(file.inputStream, file.name)
        Sheet sheet = workbook.getSheetAt(0)
        //TODO: Finish implementation
        Iterator<Row> iterator = sheet.iterator()

        while (iterator.hasNext()) {
            Row nextRow = iterator.next()
            Iterator<Cell> cellIterator = nextRow.cellIterator()
            Bookmark bookmark = new Bookmark()

            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next()
                int columnIndex = nextCell.getColumnIndex()

                switch (columnIndex) {
                    case 0:
                        bookmark.url = nextCell.stringCellValue
                        break
                    case 1:
                        bookmark.name = nextCell.stringCellValue
                        break
                    case 2:
                        bookmark.description = nextCell.stringCellValue
                        break
                }


            }
            bookmarks.add(bookmark)
        }
        //Iterate over rows
        //Create new categories
        //Create collection of bookmarks

        if(bookmarks) {
            // Per specification, flush the DB and replace with imported data.
            bookmarkRepository.deleteAll()
            bookmarkRepository.save(bookmarks)
        }

        log.info("Bookmark import completed.")
    }

    void exportBookmarks(OutputStream outputStream) {
        log.info("Beginning bookmark export. This may take some time.")
        List<Bookmark> bookmarks = bookmarkRepository.findAll()
        Workbook workbook = new XSSFWorkbook()
        XSSFSheet sheet = workbook.createSheet("bookmarks")

        bookmarks.eachWithIndex { bookmark, idx ->
            XSSFRow row = sheet.createRow(idx)
            row.createCell(0).setCellValue(bookmark.url)
            row.createCell(1).setCellValue(bookmark.name)
            row.createCell(2).setCellValue(bookmark.description)
            row.createCell(3).setCellValue(bookmark.bookmarkCategory.name)
            row.createCell(4).setCellValue(bookmark.subcategory.name)
        }
        log.info("Bookmark export completed.")

        workbook.write(outputStream)
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
