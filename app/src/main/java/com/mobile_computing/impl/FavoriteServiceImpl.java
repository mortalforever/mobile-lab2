package com.mobile_computing.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mobile_computing.FavoriteService;
import com.mobile_computing.MobileComputingApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the favorite service
 * @author dxh
 * @date 2024/1/31
 */
public class FavoriteServiceImpl implements FavoriteService {
    private static final SharedPreferences preferences;

    // cache the list of favorite books
    private static List<String> cacheList = null;

    public static final String FAVORITES_BOOKS = "favorite_books";

    static {
        Context context = MobileComputingApplication.getInstance();
        preferences = context.getSharedPreferences(MobileComputingApplication.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Retrieves the list of IDs marked as favorite from Shared Preferences.
     * @return the list of IDs marked as favorite.
     */
    @Override
    public List<String> queryFavoriteList() {
        if (cacheList == null) {
            if (preferences.contains(FAVORITES_BOOKS)) {
                String favoriteBooks = preferences.getString(FAVORITES_BOOKS, "");
                if (favoriteBooks != null) {
                    cacheList = new ArrayList<>(Arrays.asList(favoriteBooks.split(";")));
                    return cacheList;
                }
            }
            cacheList = new ArrayList<>();
        }
        return cacheList;
    }

    /**
     * Updates the list of IDs marked as favorite in Shared Preferences.
     * @param favoriteList the list of IDs marked as favorite.
     */
    @Override
    public void setFavoriteList(List<String> favoriteList) {
        String favoriteBooks = String.join(";", favoriteList);
        // apply() is asynchronous, commit() is synchronous
        preferences.edit().putString(FAVORITES_BOOKS, favoriteBooks).apply();
    }

    /**
     * When a user marks an item as favorite, adds the ID to the list of favorites and updates it in Shared Preferences.
     * @param id the ID of the book marked as favorite.
     * @return true means success, false otherwise
     */
    @Override
    public boolean addBook2FavoriteList(int id) {
        List<String> queriedFavoriteList = queryFavoriteList();
        if (!queriedFavoriteList.contains(String.valueOf(id))) {
            queriedFavoriteList.add(String.valueOf(id));
            setFavoriteList(queriedFavoriteList);
            return true;
        }
        return false;
    }

    /**
     * When a user cancel star on the book, removes the ID from the list of favorites and updates it in Shared Preferences.
     * @param id the ID of the book marked as favorite.
     * @return true means success, false otherwise
     */
    @Override
    public boolean removeBookFromFavoriteList(int id) {
        List<String> queriedFavoriteList = queryFavoriteList();
        if (queriedFavoriteList.contains(String.valueOf(id))) {
            queriedFavoriteList.remove(String.valueOf(id));
            setFavoriteList(queriedFavoriteList);
            return true;
        }
        return false;
    }
}
