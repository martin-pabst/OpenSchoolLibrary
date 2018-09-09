create table bookshop(
  id bigserial primary key,
  school_id bigint references school(id),
  name varchar(100),
  address_id bigint references address(id),
  active boolean
);

create table bookorder (
  id bigserial primary key,
  school_id bigint references school(id),
  bookshop_id bigint references bookshop(id),
  orderdate date
);

create table bookorderposition(
  id bigserial primary key,
  bookorder_id bigint references bookorder (id),
  book_id bigint references book(id),
  number int
);