package com.morgan.design.helpers;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactsLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ContactsLoader.class);

    public static List<String> getAllContactNames(ContentResolver contentResolver) {
        Set<String> lContactNamesList = new HashSet<>();
        Cursor lPeople = null;
        try {
            String[] projection = new String[]{Data._ID, ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_THUMBNAIL_URI};

            // Get all Contacts
            lPeople = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
            if (lPeople != null) {
                while (lPeople.moveToNext()) {
                    // Add Contact's Name into the List
                    int index = lPeople.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    String value = lPeople.getString(index);
                    LOG.debug("Lookup index {} : value {}", index, value);
                    if (value != null) {
                        lContactNamesList.add(value);
                    }
                }
            }
        } catch (NullPointerException e) {
            LOG.error("Unable to get contacts", e.getMessage());
        } finally {
            if (lPeople != null) {
                try {
                    lPeople.close();
                } catch (Exception e) {
                    LOG.error("Failed to close contacts cursor", e.getMessage());
                }
            }
        }
        List<String> values = new ArrayList<>();
        values.addAll(lContactNamesList);
        return values;
    }

}
