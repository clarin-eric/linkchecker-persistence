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