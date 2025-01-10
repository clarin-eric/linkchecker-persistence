# version 2.0.0
- based on Java 21
- upgrading Spring Boot to 3.4.1

# version 1.0.3
- upgrading Spring Boot to 3.3.3

# version 1.0.2
- upgrading Spring Boot to 3.2.5

# version 1.0.1
- upgrading Spring Boot to 3.2.2

# version 1.0.0
- upgrading Spring Boot to 3.1.4

# version 0.0.7
- GenericRepository with findAll method, to pass an SQL/JPQL query as parameter (issue #18) and to receive a Stream of Tuple

# version 0.0.6
- bug fix for issue #10, #11, #12, #15
- introducing new class GenericRepository, which allows queries as method parameter  
- adding delete methods for history- and obsolete table and purge methods to service (issue #13) 

# version 0.0.5
- dependency upgrade of spring-boot-starter-parent to v2.7.10

# version 0.0.4
- shifting prioritization from status- to url table

# version 0.0.3
- excluding mapping fields from toString() in class Url, Context and Providergroup to prevent stack overflow
- correction in schema.sql for history table for fields message and content_length

# version 0.0.2
- modifications in schema.sql
-- enlarging field context.origin from VARCHAR(256) to VARCHAR(512)
-- changing type of obsolete.client_name from INT to VARCHAR(256)
-- renaming  aggregated_status.number to aggregated_status.number_id and adding field  aggregated_status.number_duration
- corrections in script /add-on/transferDB.sql
- corrections in model
-- adding index definition to UrlContext.class
-- adapting AggregatedStatus.class to modified schema
- corrections, modifications and additions in repository
-- changing parameter of HistoryRepository.saveHistoryLinksOlderThan from int periodOfDays to LocalDateTime
-- using custom queries in StatusDetailRepository for methods findAllByCategory and findAllByProvidergroupnameAndCategory
-- changing parameter of StatusRepository.saveStatusLinksOlderThan from int periodOfDays to LocalDateTime
-- using custom query UrlConext.findByUrlAndContextAndExpectedMimeType
- corrections, modifications and additions in service
-- adding methods StatusService.findAllDetail(Category category) and StatusService.findAllDetail(String providergroupname, Category category)
-- modifications in LinkService.class for better performance