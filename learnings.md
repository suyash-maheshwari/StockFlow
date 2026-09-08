# StockFlow — Learnings & Design Decisions

This file tracks real problems I hit while building this project, how I solved them,
and design decisions I deliberately deferred — so I don't forget the reasoning later,
and so it's easy to explain in interviews.

## Bugs Fixed & Why They Happened

### ErrorResponse serialized as empty {}
- **Problem**: Jackson couldn't serialize private fields with no getters.
- **Fix**: Added @Getter (Lombok) to ErrorResponse.
- **Lesson**: Jackson needs public getters, not just fields, to serialize a class.

### Auto-increment column conflict on Category table
- **Problem**: Renamed the @Id column while using ddl-auto=update; Hibernate tried to
  ADD a second auto-increment column instead of replacing the old one, and MySQL rejected it.
- **Fix**: Dropped the table and let Hibernate recreate it (safe since no real data existed yet).
- **Lesson**: ddl-auto=update can't handle structural changes like renaming a primary key.
  Real systems use Flyway/Liquibase for this instead.

## Deferred Design Decisions

### Admin visibility into deactivated products
- getProductById/getAllProducts filter by active=true, so deactivated products are
  invisible to everyone, including admins.
- Revisit once Spring Security + roles exist: add an admin-only endpoint that bypasses
  the active filter, protected by @PreAuthorize("hasRole('ADMIN')").

### Category hierarchy (flat vs. subcategories)
- Went with flat categories + a nullable parentCategory field, so subcategories can be
  added later without a schema migration, without building the tree logic now.

## Future Features (build after Spring Security)
- Admin endpoint: view deactivated products (GET /products/deactivated)
- Admin endpoint: reactivate a product (PATCH /products/{id}/reactivate)
- Both need @PreAuthorize("hasRole('ADMIN')") from the moment they're created —
  do not build them unprotected first.