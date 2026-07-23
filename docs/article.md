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