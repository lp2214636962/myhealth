package com.itheima.test;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
    IText报表测试
 */
public class ITextPDFTest {
    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document();
        // 报表生成位置
        PdfWriter.getInstance(document,new FileOutputStream("D:\\TEPL\\ceshi.pdf"));
        document.open();
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        document.add(new Paragraph("妹妹你好!",new Font(baseFont)));
        document.close();
    }
}
