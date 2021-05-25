package com.crashlibrary.util;

/**
 * Title:字符串工具类
 *
 * description:
 * autor:pei
 * created on 2021/5/25
 */
public class StringUtil {

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || input.trim().length() == 0 || input.equals("null"))
            return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    /***
     * 将字符串中首个target替换成replacement
     *
     * @param source 操作源字符串
     * @param target 要替换的字符串
     * @param replacement 替换值
     *
     * @return 替换成功返回具体字符串,替换失败返回 null
     */
    public static String replaceByTarget(String source,String target,String replacement){
        if(StringUtil.isNotEmpty(source)&&StringUtil.isNotEmpty(target)&&StringUtil.isNotEmpty(replacement)){
            if(source.contains(target)){
                int index=source.indexOf(target);
                StringBuffer buffer = new StringBuffer(source);
                buffer.replace(index, index+1, replacement);
                return buffer.toString();
            }
        }
        return null;
    }

}
