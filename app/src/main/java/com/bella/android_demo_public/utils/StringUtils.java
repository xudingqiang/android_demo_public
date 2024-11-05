/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bella.android_demo_public.utils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {
    /**
     * 非空判斷
     *
     * @param ojb
     * @return
     */
    public static String ToString(Object ojb) {
        if (ojb == null) {
            return "";
        } else {
            return String.valueOf(ojb).trim();
        }
    }

    public static String ToString(Object ojb, String str) {
        if (ojb == null) {
            return str;
        } else {
            return String.valueOf(ojb).trim();
        }
    }


    public static Long ToLong(Object ojb) {
        if (ojb == null) {
            return 0L;
        } else {
            String str = ToString(ojb);
            LogTool.i("str  "+str);
            if (str.contains(".")) {
                String[] arr = str.split("\\.");
                str = arr[0];
            }
            return Long.valueOf(str);
        }
    }

    public static int ToInt(Object ojb) {
        if (ojb == null) {
            return 0;
        } else {
            return ToDouble(ojb).intValue();
        }
    }

    public static boolean ToBoolean(Object ojb) {
        if (ojb == null) {
            return false;
        } else {
            return Boolean.valueOf(ToString(ojb));
        }
    }

    /**
     * @param date
     * @param IntegerDigits
     * @param FractionDigits
     * @return
     */
    public static String getPercentFormat(double date, int IntegerDigits, int FractionDigits) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumIntegerDigits(IntegerDigits);//小数点前保留几位
        nf.setMinimumFractionDigits(FractionDigits);// 小数点后保留几位
        String str = nf.format(date);
        return str;
    }


    public static Double ToDouble(Object ojb) {
        if (ojb == null) {
            return 0.0;
        } else {
            return Double.valueOf(ToString(ojb));
        }
    }

    public static Float ToFloat(Object ojb) {
        try {
            if (ojb == null) {
                return 0f;
            } else {
                return Float.valueOf(ToString(ojb));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0f;
        }
    }


    public static String ToDecimal(Object value) {
        double time = ToDouble(value);
        return new BigDecimal(time).toPlainString();
    }


    //传入时间戳即可
    public static String conversionTime(Object timeStamp) {
        try {
            //yyyy-MM-dd HH:mm:ss 转换的时间格式  可以自定义
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //转换
            String time = sdf.format(new Date(ToLong(timeStamp)));
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Long convertDateStringToLong(String dateString, String formatStr) {
        try {
            DateFormat format = new SimpleDateFormat(formatStr);
            Date date = format.parse(dateString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    //传入指定时间
    public long convertToTimestamp(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            long timestamp = cal.getTimeInMillis();
            return timestamp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long conversionToSeconds(String timeString) {
        try {
            if (timeString == null) {
                return 0;
            } else {
                String[] parts = timeString.split(":");
                int hours = 0;
                int minutes = 0;
                int seconds = 0;
                if (parts.length > 2) {
                    hours = ToInt(parts[0]);
                    minutes = ToInt(parts[1]);
                    seconds = ToInt(parts[2]);
                } else if (parts.length > 1) {
                    minutes = ToInt(parts[0]);
                    seconds = ToInt(parts[0]);
                } else {
                    seconds = ToInt(parts[0]);
                }

                int totalSeconds = hours * 3600 + minutes * 60 + seconds;
                return totalSeconds;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String getPercentFormat(Object data, int IntegerDigits, int FractionDigits) {
        try {
            double date = ToDouble(data);
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMaximumIntegerDigits(IntegerDigits);//小数点前保留几位
            nf.setMinimumFractionDigits(FractionDigits);// 小数点后保留几位
            String str = nf.format(date);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0%";
    }

    public static String dateChange(String inputDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate date = LocalDate.parse(inputDate, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(outputFormatter);
        return formattedDate;
    }

    public static String conversionDateToTime(String dateTimeString) {
        String timePattern = "HH:mm:ss"; // 我们想要的时间格式
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat(timePattern);

        try {
            // 解析日期时间字符串
            Date date = inputFormat.parse(dateTimeString);
            // 使用输出格式格式化日期对象
            String timeString = outputFormat.format(date);
            return timeString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTimeString;
    }


    public static String ToPlainString(Object object) {
        if (object == null) {
            return "0";
        } else {
            BigDecimal bd = new BigDecimal(ToString(object));
            return bd.toPlainString();
        }
    }

    /*
     * 将中文字符串转成UTF-8
     */
    public static String to_Utf(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String long2Date(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        String str = sdf.format(date);
        return str;
    }

    public static String long2ALLDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(time);
        String str = sdf.format(date);
        return str;
    }


}
