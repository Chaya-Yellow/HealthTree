package com.jks.Spo2MonitorEx.app.share;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class ExcelUtils {
        public static WritableFont arial14font = null;

        public static WritableCellFormat arial14format = null;
        public static WritableFont arial10font = null;
        public static WritableCellFormat arial10format = null;
        public static WritableFont arial12font = null;
        public static WritableCellFormat arial12format = null;
        public static WritableFont arial12font_cxf_high = null;
        public static WritableFont arial12font_cxf_low = null;
        public static WritableCellFormat arial12format_cxf_high = null;
        public static WritableCellFormat arial12format_cxf_low = null;

        public final static String UTF8_ENCODING = "UTF-8";
        public final static String GBK_ENCODING = "GBK";



    public static void format() {
            try {
                arial14font = new WritableFont(WritableFont.ARIAL, 14,
                        WritableFont.BOLD);
                //arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
                arial14font.setColour(Colour.BLUE);
                arial14format = new WritableCellFormat(arial14font);
                arial14format.setAlignment(jxl.format.Alignment.CENTRE);
                arial14format.setBorder(jxl.format.Border.ALL,
                        jxl.format.BorderLineStyle.THIN);
                arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
                arial10font = new WritableFont(WritableFont.ARIAL, 10,
                        WritableFont.BOLD);
                arial10format = new WritableCellFormat(arial10font);
                arial10format.setAlignment(jxl.format.Alignment.CENTRE);
                arial10format.setBorder(jxl.format.Border.ALL,
                        jxl.format.BorderLineStyle.THIN);
                arial10format.setBackground(jxl.format.Colour.GREY_25_PERCENT);
                arial12font = new WritableFont(WritableFont.ARIAL, 12);
                arial12format = new WritableCellFormat(arial12font);
                arial12format.setBorder(jxl.format.Border.ALL,
                        jxl.format.BorderLineStyle.THIN);
                arial12font_cxf_high = new WritableFont(WritableFont.ARIAL, 12);
                arial12font_cxf_high.setColour(Colour.RED);
                arial12format_cxf_high = new WritableCellFormat(arial12font_cxf_high);
                arial12format_cxf_high.setBorder(jxl.format.Border.ALL,
                        jxl.format.BorderLineStyle.THIN);
                arial12font_cxf_low = new WritableFont(WritableFont.ARIAL, 12);
                arial12font_cxf_low.setColour(Colour.LIGHT_BLUE);
                arial12format_cxf_low = new WritableCellFormat(arial12font_cxf_low);
                arial12format_cxf_low.setBorder(jxl.format.Border.ALL,
                        jxl.format.BorderLineStyle.THIN);

            }
            catch (WriteException e) {
                e.printStackTrace();
            }
        }

        public static void initExcel(File file, String[] colName) {
            format();
            WritableWorkbook workbook = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                workbook = Workbook.createWorkbook(file);
                WritableSheet sheet = workbook.createSheet("DataSheet", 0);
//
//                for (int col =0;col<2;col++) {
//
//                    sheet.addCell(new Label(col, 0, titletip[col], arial14format));
//                }
//                for (int j = 0;j<7;j++) {
//                        sheet.addCell(new Label(0, 1+j, resulttext[j], arial14format));
//                        sheet.addCell(new Label(1, 1+j, tiptext[j], arial14format));
//                }


                for (int col = 0; col < colName.length; col++) {
                    sheet.addCell(new Label(col, 0, colName[col], arial10format));
                }
                workbook.write();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        @SuppressWarnings("unchecked")
        public static <T> void writeObjListToExcel(List<T> objList, File file) {
            if (objList != null && objList.size() > 0) {
                WritableWorkbook writebook = null;
                InputStream in = null;
                try {
                    WorkbookSettings setEncode = new WorkbookSettings();
                    setEncode.setEncoding(UTF8_ENCODING);
                    in = new FileInputStream(file);
                    Workbook workbook = Workbook.getWorkbook(in);
                    writebook = Workbook.createWorkbook(file, workbook);
                    WritableSheet sheet = writebook.getSheet(0);
                    for (int j = 0; j < objList.size(); j++) {
                        ArrayList<String> list = (ArrayList<String>) objList.get(j);
                        for (int i = 0; i < list.size(); i++) {
                            if(i==2) {    //sbp>140 red  sbp<90 blue
                                if ( Float.valueOf(list.get(i)) >= 140.0)
                                {
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format_cxf_high));
                                }
                                else if (Float.valueOf(list.get(i)) < 90) {
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format_cxf_low));
                                }
                                else{
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format));
                                }

                            }
                            else if (i==3)/////dia >90  red  dia<50  blue
                            {
                                if (Float.valueOf(list.get(i)) >= 90) {
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format_cxf_high));
                                }
                                else if (Float.valueOf(list.get(i)) < 50) {
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format_cxf_low));
                                }
                                else{
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format));
                                }
                            }
                            else if(i==4) {  //////HR>100 red   HR<60 BLUE
                                if (Float.valueOf(list.get(i)) >= 100) {
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format_cxf_high));
                                }
                                else if (Float.valueOf(list.get(i)) < 60) {
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format_cxf_low));
                                }
                                else{
                                    sheet.addCell(new Label(i, j+1, list.get(i),
                                            arial12format));
                                }
                            }
                            else {//////其他正常显示
                                sheet.addCell(new Label(i, j+1, list.get(i),
                                        arial12format));
                            }
                        }
                    }
//                    ///////tip
//                    for (int j = 0; j < objList1.size(); j++) {
//                        ArrayList<String> list = (ArrayList<String>) objList1.get(j);
//                        for (int i = 0; i < list.size(); i++) {
//                            if (j==0||j==1) {
//                                sheet.addCell(new Label(i, j + objList.size() + 4, list.get(i),
//                                        arial10format));///////增加位移
//                            }
//                            else {
//                                sheet.addCell(new Label(i, j + objList.size() + 4, list.get(i),
//                                        arial12format));///////增加位移
//                            }
//                        }
//                    }
                    writebook.write();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (writebook != null) {
                        try {
                            writebook.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        public static  List<BillObject> read2DB(File f, Context con) {
            ArrayList<BillObject> billList = new ArrayList<BillObject>();
            try {
                Workbook course = null;
                course = Workbook.getWorkbook(f);
                Sheet sheet = course.getSheet(0);

                Cell cell = null;
                for (int i = 1; i < sheet.getRows(); i++) {
                    BillObject tc = new BillObject();
                    cell = sheet.getCell(1, i);
                    tc.setFood(cell.getContents());
                    cell = sheet.getCell(2, i);
                    tc.setClothes(cell.getContents());
                    cell = sheet.getCell(3, i);
                    tc.setHouse(cell.getContents());
                    cell = sheet.getCell(4, i);
                    tc.setVehicle(cell.getContents());
                    Log.d("gaolei", "Row"+i+"---------"+tc.getFood() + tc.getClothes()
                            + tc.getHouse() + tc.getVehicle());
                    billList.add(tc);

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return billList;
        }
        public static Object getValueByRef(Class cls, String fieldName) {
            Object value = null;
            fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
                    .substring(0, 1).toUpperCase());
            String getMethodName = "get" + fieldName;
            try {
                Method method = cls.getMethod(getMethodName);
                value = method.invoke(cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }
    }


