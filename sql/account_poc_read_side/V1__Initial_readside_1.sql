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