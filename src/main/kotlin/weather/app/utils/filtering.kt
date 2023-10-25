package weather.app.utils

import jakarta.persistence.criteria.*
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification

enum class FilterType {
    EQ, IN,
}

data class FilterRule(
    val fieldName: String,
    val value: Any,
    val type: FilterType,
    val ignoreCase: Boolean = false
)

class FilterSpecification<T>(val filters: List<FilterRule>) : Specification<T> {

    override fun toPredicate(root: Root<T>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate {
        val predicates = mutableListOf<Predicate>()

        filters.forEach { (fieldName, value, type, ignoreCase) ->
            when (type) {
                FilterType.EQ -> {
                    val path: Path<T> = root.get(fieldName)
                    predicates.add(builder.equal(path, value))
                }

                FilterType.IN -> {
                    if (ignoreCase) {
                        val lowered = (value as Collection<*>).map { (it as String).lowercase() }
                        predicates.add(builder.lower(root.get(fieldName)).`in`(lowered))
                    } else {
                        predicates.add(root.get<T>(fieldName).`in`(value as Collection<*>))
                    }
                }
            }
        }

        return builder.and(*predicates.toTypedArray())
    }
}

fun prepareFilters(vararg filer: FilterData): List<FilterRule> {
    return filer.filter { it.value != null }.map {
        FilterRule(
            fieldName = it.fieldName,
            value = it.value!!,
            type = it.type,
            ignoreCase = it.ignoreCase,
        )
    }
}

fun prepareSort(vararg parameters: SortData): Sort {
    val allowedProperties = listOf("date", "id", "-date", "-id")

    val orders = parameters
        .filter { allowedProperties.contains(it.value) }
        .map {
            if (it.value!!.startsWith("-")) {
                Sort.Order.desc(it.value.substring(1))
            } else {
                Sort.Order.asc(it.value)
            }
        }
    return Sort.by(orders)
}

data class FilterData(
    val fieldName: String,
    val value: Any?,
    val type: FilterType,
    val ignoreCase: Boolean = false
)

data class SortData(
    val value: String?,
)
