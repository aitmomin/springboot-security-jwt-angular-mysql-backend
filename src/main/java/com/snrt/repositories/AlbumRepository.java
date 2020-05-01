package com.snrt.repositories;

import com.snrt.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    public List<Album> getAlbumByUserAlbumId(long id);
    @Query(value="SELECT DISTINCT a.*" +
            "    FROM album a, track t" +
            "    WHERE a.id=t.id_album and a.id_user= ?1 and (SELECT COUNT(*) FROM track tr WHERE tr.id_album = a.id) <> 0", nativeQuery = true)
    public List<Album> getAlbumsByArtist(long id);
    @Query(value="SELECT DISTINCT album_name" +
            "    FROM album a, track t" +
            "    WHERE a.id=t.id_album and a.id_user= ?1 and track_count > (SELECT COUNT(*) FROM track tr WHERE tr.id_album = a.id)", nativeQuery = true)
    public List<Album> getAlbumsByConnected(long id);
    public List<Album> getAlbumNameAndViewsAndRatingByUserAlbumId(long id);
    //@Query(value="SELECT views FROM album WHERE id_user = ?1", nativeQuery = true)
    //public long[] getViewsByUserAlbumId(long id);
    //@Query(value="SELECT rating FROM album WHERE id_user = ?1", nativeQuery = true)
    //public long[] getRatingByUserAlbumId(long id);
    public long countByUserAlbumId(long id);
    @Query(value="SELECT sum(views) FROM album WHERE id_user = ?1 GROUP BY id_user", nativeQuery = true)
    public long sumViewsByUserAlbumId(long id);
    @Query(value="SELECT sum(rating) FROM album WHERE id_user = ?1 GROUP BY id_user", nativeQuery = true)
    public long sumRatingByUserAlbumId(long id);


    /* test track count =
    SELECT DISTINCT album_name
    FROM album a, track t
    WHERE a.id=t.id_album and a.id_user=1 and track_count > (SELECT COUNT(*) FROM track tr WHERE tr.id_album = a.id)
    * */

    /* if tracks existe
    SELECT DISTINCT a.id
    FROM album a, track t
    WHERE a.id=t.id_album and a.id_user=1 and (SELECT COUNT(*) FROM track tr WHERE tr.id_album = a.id) <> 0
    * */
}
