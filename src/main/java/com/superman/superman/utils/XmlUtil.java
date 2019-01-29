package com.superman.superman.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtil {

    public Document parseFromUrl(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(url);
    }

    public static Document parseFromText(String text) throws DocumentException {
        return DocumentHelper.parseText(text);
    }

    public static  Map<String,String> treeWalkStart(String text) throws DocumentException {
        Document document = parseFromText(text);
        Element rootElement = document.getRootElement();
        Map<String,String> keyval = new HashMap<String,String>();
        Element nonce_str = rootElement.element("nonce_str");
        Element prepay_id = rootElement.element("prepay_id");
        keyval.put("nonce_str",nonce_str.getText());
        keyval.put("prepay_id",prepay_id.getText());
        return keyval;
    }
    
    public static  Map<String,String> doXMLParse(String text) throws DocumentException {
        Document document = parseFromText(text);
        Element rootElement = document.getRootElement();
        Map<String,String> keyval = new HashMap<String,String>();
        Element nonce_str = rootElement.element("result_code");
        Element prepay_id = rootElement.element("return_code");
        Element out_trade_no = rootElement.element("out_trade_no");
        Element attach = rootElement.element("attach");
        keyval.put("result_code",nonce_str.getText());
        keyval.put("return_code",prepay_id.getText());
        keyval.put("out_trade_no",out_trade_no.getText());
        keyval.put("attach",attach.getText());
        return keyval;
    }
    
}
