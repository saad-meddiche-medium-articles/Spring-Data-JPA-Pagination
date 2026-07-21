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

# Slice

# Window (keyset / cursor pagination)

# OFFSET
