# The question that sparked this demo project
- I was maintaining a project, and I noticed some latency in a paginated API, particularly on deep pages.
- That's when I wondered why the first **(shallow)** pages were fast, but the last **(deep)** ones were slower ?

# The investigation phase
- I checked the code source and found the usual problems (N+1 queries, lack of projection, no indexes ...).
- After fixing these "usual" issues, I noticed a significant improvement in latency, **but the deep pages remained slower than first ones.**
- This mean there's something I'm missing !!

# Note
- This latency didn't cause any issues in production, since the user applies a filter that generates either a single page or several dozens pages.
- BUTTT The curious me wanted to understand the "why" !!

# The tests
- I created this demo to run tests against a PostgresSQL dataset seeded with ~11M persons and ~22M products, with each person having two products.

## Page<T>

1. **Simple Page<T>**

- Produced two queries, one to retrieve the requested page 
and second is a count query to get the total items to calculate total pages.
But the latency varies based on the page number


- Requesting `page 0` with a `size of 10` items:
  - Retrieval query took `0 Minutes : 0.015 Seconds`
  - Count query took `0 Minutes : 21.787 Seconds`

```
2026-07-23T10:39:01.502+01:00 DEBUG 5868 --- [spring-data-jpa-pagination] [omcat-handler-0] n.t.d.l.l.SLF4JQueryLoggingListener      :
Name:dataSource, Connection:4, Time:15, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date,count(p2_0.id) from person p1_0 left join product p2_0 on p1_0.id=p2_0.owner_id where (?=true or p1_0.full_name like (?||'%') escape '') and (?=true or p1_0.birth_date>=?) and (?=true or p1_0.birth_date<=?) group by p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date order by p1_0.id fetch first ? rows only"]
Params:[(true,NULL(VARCHAR),true,NULL(DATE),true,NULL(DATE),10)]

2026-07-23T10:39:23.306+01:00 DEBUG 5868 --- [spring-data-jpa-pagination] [omcat-handler-0] n.t.d.l.l.SLF4JQueryLoggingListener      :
Name:dataSource, Connection:4, Time:21787, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select count(p1_0.id) from person p1_0 left join product p2_0 on p1_0.id=p2_0.owner_id where (?=true or p1_0.full_name like (?||'%') escape '') and (?=true or p1_0.birth_date>=?) and (?=true or p1_0.birth_date<=?) group by p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date"]
Params:[(true,NULL(VARCHAR),true,NULL(DATE),true,NULL(DATE))]
```

- Requesting `page 1_110_000` with a `size of 10` items:
    - Retrieval query took `3 Minutes : 39.219 Seconds`
    - Count query took `0 Minutes : 24.865 Seconds`
    - Also, higher disk read activity ~~(I won't go into detail about database usage statistic, as I'm focusing on the latency here. Feel free to run the demo locally and conduct your own tests)~~

```
2026-07-23T11:44:05.300+01:00 DEBUG 5868 --- [spring-data-jpa-pagination] [omcat-handler-9] n.t.d.l.l.SLF4JQueryLoggingListener      :
Name:dataSource, Connection:7, Time:219219, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date,count(p2_0.id) from person p1_0 left join product p2_0 on p1_0.id=p2_0.owner_id where (?=true or p1_0.full_name like (?||'%') escape '') and (?=true or p1_0.birth_date>=?) and (?=true or p1_0.birth_date<=?) group by p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date order by p1_0.id offset ? rows fetch first ? rows only"]
Params:[(true,NULL(VARCHAR),true,NULL(DATE),true,NULL(DATE),11099990,10)]

2026-07-23T11:44:30.176+01:00 DEBUG 5868 --- [spring-data-jpa-pagination] [omcat-handler-9] n.t.d.l.l.SLF4JQueryLoggingListener      :
Name:dataSource, Connection:7, Time:24865, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select count(p1_0.id) from person p1_0 left join product p2_0 on p1_0.id=p2_0.owner_id where (?=true or p1_0.full_name like (?||'%') escape '') and (?=true or p1_0.birth_date>=?) and (?=true or p1_0.birth_date<=?) group by p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date"]
Params:[(true,NULL(VARCHAR),true,NULL(DATE),true,NULL(DATE))]
```

- I noticed that the default count query included an `unnecessary LEFT JOIN`, we might get better results by removing that `JOIN`.

2. **Page<T> with custom count**

- In this test I have `overridden the count query` to remove the LEFT JOIN.


- Requesting `page 0` with a `size of 10` items:
  - Retrieval query took `0 Minutes : 0.002 Seconds`
  - Count query took `0 Minutes : 0.207 Seconds`

```
2026-07-23T13:23:22.513+01:00 DEBUG 28032 --- [spring-data-jpa-pagination] [mcat-handler-15] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:dataSource, Connection:11, Time:3, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date,count(p2_0.id) from person p1_0 left join product p2_0 on p1_0.id=p2_0.owner_id where (?=true or p1_0.full_name like (?||'%') escape '') and (?=true or p1_0.birth_date>=?) and (?=true or p1_0.birth_date<=?) group by p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date order by p1_0.id offset ? rows fetch first ? rows only"]
Params:[(true,NULL(VARCHAR),true,NULL(DATE),true,NULL(DATE),40,10)]

2026-07-23T13:23:22.724+01:00 DEBUG 28032 --- [spring-data-jpa-pagination] [mcat-handler-15] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:dataSource, Connection:11, Time:207, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select count(p1_0.id) from person p1_0 where (?=true or p1_0.full_name like (?||'%') escape '') and (?=true or p1_0.birth_date>=?) and (?=true or p1_0.birth_date<=?)"]
Params:[(true,NULL(VARCHAR),true,NULL(DATE),true,NULL(DATE))]
```

- Requesting `page 1_110_000` with a `size of 10` items:
  - Retrieval query took `3 Minutes : 30.366 Seconds`
  - Count query took `0 Minutes : 1.76 Seconds`
  - Also, higher disk read activity

```
Name:dataSource, Connection:12, Time:210366, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date,count(p2_0.id) from person p1_0 left join product p2_0 on p1_0.id=p2_0.owner_id where (?=true or p1_0.full_name like (?||'%') escape '') and (?=true or p1_0.birth_date>=?) and (?=true or p1_0.birth_date<=?) group by p1_0.id,p1_0.first_name,p1_0.last_name,p1_0.birth_date order by p1_0.id offset ? rows fetch first ? rows only"]
Params:[(true,NULL(VARCHAR),true,NULL(DATE),true,NULL(DATE),11099990,10)]

2026-07-23T13:29:37.962+01:00 DEBUG 28032 --- [spring-data-jpa-pagination] [mcat-handler-18] n.t.d.l.l.SLF4JQueryLoggingListener      : 
Name:dataSource, Connection:12, Time:1760, Success:True
Type:Prepared, Batch:False, QuerySize:1, BatchSize:0
Query:["select count(p1_0.id) from person p1_0 where (?=true or p1_0.full_name like (?||'%') escape '') and (?=true or p1_0.birth_date>=?) and (?=true or p1_0.birth_date<=?)"]
Params:[(true,NULL(VARCHAR),true,NULL(DATE),true,NULL(DATE))]
```

- As I expected, the count query's latency improved significantly, but that of the retrieval query remained unchanged.
- The benefit we gained from this custom count query lies in its low latency but only for the first pages; latency for last pages stayed high.