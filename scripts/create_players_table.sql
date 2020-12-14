CREATE TABLE public.players (
	id varchar(255) NOT NULL,
	injury_status varchar(255) NULL,
	"name" varchar(255) NULL,
	CONSTRAINT players_pkey PRIMARY KEY (id)
);
CREATE INDEX players_name_idx ON public.players ("name");
