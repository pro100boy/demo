package com.example.demo.utils;

import com.example.demo.model.Contact;
import com.example.demo.to.ContactTo;

import java.util.Collections;
import java.util.List;

public class ServiceUtil {

    /*
     * returns a view (not a new list) of the sourceList for the
     * range based on page and pageSize
     * @param sourceList
     * @param page
     * @param pageSize
     * @return ContactTo
     */
    public static ContactTo getPage(List<Contact> sourceList, int page, int pageSize) {
        int fromIndex = (page - 1) * pageSize;

        final ContactTo contactTo = new ContactTo();

        if (sourceList == null || sourceList.size() < fromIndex) {
            contactTo.setContacts(Collections.emptyList());
            return contactTo;
        }

        // toIndex exclusive
        contactTo.setContacts(sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size())));
        contactTo.setCurrentPage(page);
        contactTo.setTotalCount(sourceList.size());

        int totPages = sourceList.size() % pageSize == 0 ? sourceList.size()/pageSize : sourceList.size()/pageSize + 1;
        contactTo.setTotalPages(totPages);
        return contactTo;
    }
}
