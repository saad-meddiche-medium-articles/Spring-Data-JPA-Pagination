# Virtual Columns 
- These columns are similar to the **standard View**, they are calculated `on the fly` using an **immutable expression** when reading the row.
- Because they have the `on the fly` trait, there is no extra disk space.
- The read speed is slower, due to extra compute in each read request.
- Can't be indexed

# Stored Columns
- These columns are similar to the **materialized View**, they are pre-calculate and saved after writing requests.
- Because there has the `pre-calculated` trait, there is extra disk space.
- The write speed is slower, due to extra compute before each write request.
- Can be indexed

# B-Tree index

# GIN trigram index

# Pagination
Different ways to fetch large datasets

## Page
- It executes two queries, one to retrieve the requested page `LIMIT 10 OFFSET 50`
and second is a count query `COUNT(*)` to get total items to calculate total pages
- **(+)**
  - provides `full navigational context`: total items and total pages
  - possibility of jumping directly to a `specific page`
- **(-)**
  - the **count query causes high latency** when scanning a large dataset
  - **deep offset request** `LIMIT 20 OFFSET 6_000_000` causes high latency because THE DB need to scan 6M rows and discard them
to get the 20 targeted items.

## Slice
- It executes one query to retrieve the requested slice `LIMIT 10+1 OFFSET 50`
- It adds '1' to the limit, to determine if there is a next slice .
- It uses the current page to determine if there is a previous slice.
- **(+)**
- **(-)**

## Window (keyset / cursor pagination)
- It executes one query to retrieve the requested window.
- It uses a keyset/cursor from the last item `WHERE (:lastSeenId IS NULL OR p.id > :lastSeenId) ORDER BY p.id`
in the previous fetch to navigate directly to next window
- **(+)**
- **(-)**
