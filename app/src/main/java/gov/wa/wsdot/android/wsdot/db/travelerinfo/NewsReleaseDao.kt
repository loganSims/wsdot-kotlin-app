package gov.wa.wsdot.android.wsdot.db.travelerinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class NewsReleaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertNews(newsItems: List<NewsRelease>)



}