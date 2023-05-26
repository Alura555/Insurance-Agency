package com.gitlab.alura.insuranceagency.service;

import java.util.Calendar;
import java.util.Date;

public class BaseClassTest {

    protected static final String DOCUMENT_TITLE_1 = "type1";
    protected static final String DOCUMENT_TITLE_2 = "type2";
    protected static final String DOCUMENT_TITLE_3 = "type3";
    protected static final String INSURANCE_TITLE_1 = "type1";
    protected static final String INSURANCE_TITLE_2 = "type2";
    protected static final String INSURANCE_TITLE_3 = "type3";
    protected static final String USER_EMAIL = "user@example.com";
    protected static final String MANAGER_EMAIL = "manager@example.com";
    protected static final String CLIENT_EMAIL = "client@example.com";
    protected static final Long DOCUMENT_TYPE_ID = 1L;
    protected static final Long INSURANCE_TYPE_ID = 1L;
    protected static final Long OFFER_ID = 1L;
    protected static final Long POLICY_ID = 2L;
    protected static final Long APPLICATION_ID = 1L;
    protected static final Long USER_ID = 1L;
    protected static final Long COMPANY_ID = 2L;
    protected static final String USERNAME = "username";
    protected static final String PASSWORD = "password";
    protected static final String ROLE_TITLE_MANAGER = "MANAGER";
    protected static final String ROLE_TITLE_CLIENT = "CLIENT";
    protected static final String ROLE_TITLE_COMPANY_MANAGER = "COMPANY MANAGER";
    protected static final String ACTION_APPROVE = "approve";
    protected static final String ACTION_REJECT = "reject";
    protected static final String DOCUMENT_NUMBER = "23455";

    protected static Date addYears(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    protected static Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
}
