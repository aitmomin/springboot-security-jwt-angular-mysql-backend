package com.snrt.repositories;

import com.snrt.entities.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    public List<Track> getTracksByAlbumIdOrderByTrackNumber(long id);
    public List<Track> getTracksByUserTrackIdOrderByTrackNumber(long id);
    public long countByUserTrackId(long id);
    @Query(value="SELECT sum(views) FROM track WHERE id_user = ?1 GROUP BY id_user", nativeQuery = true)
    public long sumViewsByUserTrackId(long id);
    @Query(value="SELECT sum(rating) FROM track WHERE id_user = ?1 GROUP BY id_user", nativeQuery = true)
    public long sumRatingByUserTrackId(long id);
    public List<Track> getTrackTitleAndViewsAndRatingByUserTrackIdOrderByTrackNumber(long id);
    public List<Track> getTrackTitleAndViewsAndRatingByAlbumIdOrderByTrackNumber(long id);

    @Query(value="SELECT t.track_title as 'track', t.time as 'time' , u.nickname as 'nickname', a.album_name as 'album', a.gender as 'gender', a.id as 'albumID', t.id as 'trackID', t.views as 'views', t.rating as 'rating'" +
            " FROM user u, album a, track t, rating r" +
            " WHERE u.id=a.id_user AND a.id=t.id_album AND t.id=r.id_track AND r.id_user=?1" +
            " UNION" +
            " SELECT t.track_title as 'track', t.time as 'time' , u.nickname as 'nickname', null as 'album', null as 'gender' , null as 'albumID', t.id as 'trackID', t.views as 'views', t.rating as 'rating'" +
            " FROM user u, track t, rating r" +
            " WHERE u.id=t.id_user AND t.id=r.id_track AND r.id_user=?1", nativeQuery = true)
    List<Object> getPlaylist(long id);

    @Query(value="SELECT sum(price) FROM track WHERE id_user=?1", nativeQuery = true)
    double sumPriceByUserTrackId(long id);

    @Query(value="SELECT sum(price) FROM track t, album a, user u WHERE u.id=?1 and u.id=a.id_user and a.id=t.id_album", nativeQuery = true)
    double sumPriceByAlbumId(long id);

}
