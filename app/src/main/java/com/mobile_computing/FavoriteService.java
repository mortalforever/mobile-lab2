package com.mobile_computing;

import java.util.List;

/**
 * This interface defines the methods for the favorite service
 * @author dxh
 * @date 2024/1/31
 */
public interface FavoriteService {
    List<String> queryFavoriteList();
    void setFavoriteList(List<String> favoriteList);
    boolean addBook2FavoriteList(int id);
    boolean removeBookFromFavoriteList(int id);
}
