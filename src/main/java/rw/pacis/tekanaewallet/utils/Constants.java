package rw.pacis.tekanaewallet.utils;


import rw.pacis.tekanaewallet.exceptions.BadRequestException;
import rw.pacis.tekanaewallet.model.enums.ErrorCode;

import java.time.ZoneOffset;
import java.util.Locale;

public interface Constants {
    /**
     * Default Pagination Page Number
     */
    public String DEFAULT_PAGE_NUMBER = "1";


    /**
     * Default Pagination Page Size
     */
    public String DEFAULT_PAGE_SIZE = "5";


    /**
     * Maximum Page Size
     */
    public int MAX_PAGE_SIZE = 1000;

    public String TOKEN_TYPE = "Bearer";

    Locale DEFAULT_LOCALE = Locale.US;

    ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+02:00");

    /**
     * Validate Request Page Number and Page Size
     * @param pageNumber Page Number
     * @param pageSize Page Size
     */
    public static void validatePageNumberAndSize(int pageNumber, int pageSize) throws BadRequestException {
        if (pageNumber < 0) {
            throw new BadRequestException(ErrorCode.PAGE_LESS_THAN_MIN,"exceptions.badRequest.pageSize.less");
        }

        if (pageSize > Constants.MAX_PAGE_SIZE) {
            throw new BadRequestException(ErrorCode.PAGE_GREATER_THAN_MAX,"exceptions.badRequest.pageSize.greater");
        }
    }
}