package com.example.forums_backend.utils;

import com.example.forums_backend.entity.Account;
import com.example.forums_backend.repository.AccountRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeedUtil {
    private static final String DEFAULT_PATH = "classpath:seed/";

    @Autowired
    AccountRepository accountRepository;
    public static void readSeedDataFromExcel(String fileName) {
        try {
            File file = ResourceUtils.getFile(DEFAULT_PATH + fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            System.out.println("Feel like reading file is ok.");
            Sheet sheet = workbook.getSheetAt(0);
            List<Account> accounts = new ArrayList<>();
            boolean hasData = true;
            boolean isColumnName = true;
            for (Row row : sheet) {
                if (isColumnName) {
                    isColumnName = false;
                    continue;
                }
                Account account = new Account();
                for (Cell cell : row) {
                    switch (cell.getColumnIndex()) {
                        case 0:
                            if (cell.getStringCellValue() != null
                                    && cell.getStringCellValue().length() > 0) {
                                account.setId(Long.valueOf(cell.getStringCellValue()));
                            } else {
                                hasData = false;
                            }
                            break;
                        case 1:
                            if (cell.getStringCellValue() != null
                                    && cell.getStringCellValue().length() > 0) {
                                account.setEmail_verify(Boolean.parseBoolean(cell.getStringCellValue()));
                            } else {
                                hasData = false;
                            }
                            break;
                        case 2:
                            if (cell.getStringCellValue() != null
                                    && cell.getStringCellValue().length() > 0) {
                                account.setFpt_member(Boolean.parseBoolean(cell.getStringCellValue()));
                            } else {
                                hasData = false;
                            }
                            break;
//                        case 3:
//                            if (cell.getStringCellValue() != null
//                                    && cell.getStringCellValue().length() > 0) {
//                                account.s(cell.getStringCellValue());
//                            } else {
//                                hasData = false;
//                            }
//                            break;
//                        case 4:
//                            if (cell.getStringCellValue() != null
//                                    && cell.getStringCellValue().length() > 0) {
//                                student.setStatus(cell.getStringCellValue());
//                            } else {
//                                hasData = false;
//                            }
//                            break;
                    }
                }
                if (!hasData) {
                    break;
                }
                accounts.add(account);
            }
            workbook.close();
            for (Account account :
                    accounts) {
                System.out.println(account);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}