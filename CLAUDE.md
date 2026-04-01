# CLAUDE.md

## Commands
```bash
./mvnw clean install    # Build
./mvnw spring-boot:run  # Run
./mvnw test             # All tests
./mvnw test -Dtest=ClassName#methodName  # Single test
./mvnw package -DskipTests  # Package
```

## Stack
Java 21, Spring Boot 3.x, Maven, PostgreSQL (runtime), H2 (tests), Lombok, Validation, SpringDoc OpenAPI, Actuator

## TDD Rules
- Tests first, always. No impl before failing test exists.
- Red → Green → Refactor → Commit
- Commits: `feat:` / `test:` / `refactor:` / `docs:`

## Git Rules
- After every task completion, stage and commit all changes
- Never leave uncommitted changes
- Commit message format: `type: short description`
    - `feat:` new feature
    - `test:` adding tests
    - `refactor:` refactoring
    - `docs:` documentation/config
    - `chore:` build/tooling changes

## Architecture Rules
- `OrderQueueService` is always an interface — impl uses `LinkedBlockingQueue`
- `@WebMvcTest` for controllers, `@DataJpaTest` for repositories, `@SpringBootTest` for integration only
- Spring Boot 4.x: `@DataJpaTest` is at `org.springframework.boot.data.jpa.test.autoconfigure`
- Always add `@ActiveProfiles("test")` to test classes; never set explicit Hibernate dialect (auto-detected from JDBC URL)

## Domain
- Order: order_id, user_id, order_items table (separate), total_amount, status, created_at, processing_start_at, completed_at
- Status: PENDING → PROCESSING → COMPLETED

## Package Structure
com.order.orderprocessing.{entity, repository, queue, worker, service, controller, dto, metrics}

## Key Design Decisions
- Dual-write with @Transactional — DB write + queue push together
- Separate order_items table — chosen over JSONB for portability and query flexibility
- Metrics computed on the fly — single aggregation query, not cached
- Worker thread pool size configurable via application.properties
- CDC considered but rejected — overkill for this scale

## API Rules
- All endpoints under /api/v1
- Order endpoints: /api/v1/orders
- Metrics endpoint: /api/v1/metrics