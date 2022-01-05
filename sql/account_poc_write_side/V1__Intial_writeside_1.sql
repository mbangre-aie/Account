-- SEQUENCE: journal_ordering_seq


-- DROP SEQUENCE journal_ordering_seq;


CREATE SEQUENCE journal_ordering_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

-- Table: journal


-- DROP TABLE journal;


CREATE TABLE journal
(
    ordering bigint NOT NULL DEFAULT nextval('journal_ordering_seq'::regclass),
    deleted boolean NOT NULL DEFAULT false,
    persistence_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    sequence_number bigint NOT NULL,
    message bytea NOT NULL,
    tags character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT journal_pk PRIMARY KEY (persistence_id, sequence_number)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


-- Index: journal_ordering_idx


-- DROP INDEX journal_ordering_idx;


CREATE UNIQUE INDEX journal_ordering_idx
    ON journal USING btree
    (ordering)
    TABLESPACE pg_default;



-- Table: read_side_offsets


-- DROP TABLE read_side_offsets;


CREATE TABLE read_side_offsets
(
    read_side_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    tag character varying(255) COLLATE pg_catalog."default" NOT NULL,
    sequence_offset bigint,
    time_uuid_offset character(36) COLLATE pg_catalog."default",
    CONSTRAINT read_side_offsets_pk PRIMARY KEY (read_side_id, tag)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;


-- Table: snapshot


-- DROP TABLE snapshot;


CREATE TABLE snapshot
(
    persistence_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    sequence_number bigint NOT NULL,
    created bigint NOT NULL,
    snapshot bytea NOT NULL,
    CONSTRAINT snapshot_pk PRIMARY KEY (persistence_id, sequence_number)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
