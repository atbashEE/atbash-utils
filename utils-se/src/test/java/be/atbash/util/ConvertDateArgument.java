/*
 * Copyright 2014-2022 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.util;

import java.util.StringJoiner;

public class ConvertDateArgument {

    private boolean errorExpected;
    private Class<? extends Throwable> errorClass;
    private String message;
    private String testValue;
    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;
    private int seconds;

    private ConvertDateArgument(String testValue, int year, int month, int day, int hours, int minutes, int seconds) {
        errorExpected = false;
        this.testValue = testValue;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    private ConvertDateArgument(String testValue, Class<? extends Throwable> errorClass, String message) {
        errorExpected = true;
        this.testValue = testValue;
        this.errorClass = errorClass;
        this.message = message;
    }

    public boolean isErrorExpected() {
        return errorExpected;
    }

    public String getTestValue() {
        return testValue;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public Class<? extends Throwable> getErrorClass() {
        return errorClass;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConvertDateArgument.class.getSimpleName() + "[", "]")
                .add("testValue='" + testValue + "'")
                .toString();
    }

    public static ConvertDateArgument of(String testValue, int year, int month, int day, int hours, int minutes, int seconds) {
        return new ConvertDateArgument(testValue
                , year
                , month
                , day
                , hours
                , minutes
                , seconds);
    }

    public static ConvertDateArgument of(String testValue, Class<? extends Throwable> errorClass, String message) {
        return new ConvertDateArgument(testValue, errorClass, message);
    }

}
