CREATE EXTENSION "pgcrypto";

CREATE TABLE public.role
(
    id uuid NOT NULL,
    name integer,
    CONSTRAINT role_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.role
    OWNER to postgres;


CREATE TABLE public."user"
(
    id uuid NOT NULL,
    created_at timestamp without time zone,
    password character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public."user"
    OWNER to postgres;


CREATE TABLE public.transaction_history
(
    id uuid NOT NULL,
    created_at timestamp without time zone,
    operation_name character varying(255) COLLATE pg_catalog."default",
    result integer,
    user_id uuid,
    CONSTRAINT transaction_pkey PRIMARY KEY (id),
    CONSTRAINT fk8vb9nyxwuxe10hhd3bkr67h3n FOREIGN KEY (user_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.transaction_history
    OWNER to postgres;

CREATE TABLE public.user_roles
(
    users_id uuid NOT NULL,
    roles_id uuid NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (users_id, roles_id),
    CONSTRAINT fkayp8a554n3beny1eifp5ct9nt FOREIGN KEY (users_id)
        REFERENCES public."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fksoyrbfa9510yyn3n9as9pfcsx FOREIGN KEY (roles_id)
        REFERENCES public.role (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.user_roles
    OWNER to postgres;


INSERT INTO public.role
VALUES
	(gen_random_uuid(), 0);