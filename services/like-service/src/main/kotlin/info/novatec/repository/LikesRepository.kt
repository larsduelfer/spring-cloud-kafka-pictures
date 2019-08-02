package info.novatec.repository

import info.novatec.model.Likes
import io.micronaut.data.annotation.Id
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*

@JdbcRepository(dialect = Dialect.MYSQL)
interface LikesRepository:CrudRepository<Likes, UUID> {
    fun update(@Id image: UUID, likes: Long)
}