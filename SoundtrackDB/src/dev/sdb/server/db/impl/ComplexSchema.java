package dev.sdb.server.db.impl;

interface ComplexSchema {

	String PRODUCTION = "production";
	String RECORDING = "recording";

	public enum Production {
		ID("prod_id"),
		RES_TITLE("prod_res_title"),
		EPISODE("prod_episode"),
		INTERNAL("prod_internal"),
		TYPE_ID("prod_type_id"),
		EDITION_ID("prod_edition_id"),
		CATALOG("prod_catalog"),
		MAIN_RELEASE_ID("prod_main_release_id"),
		STATUS("prod_status"),
		STAFF_INFO("prod_staff_info"),
		ONLINE("prod_online");

		private final String name;

		private Production(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override public String toString() {
			return getName();
		}
	}



	public enum Recording {
		ID("rec_id"),
		TITLE("rec_title"),
		INTERNAL("rec_internal"),
		GENRE_ID("rec_genre_id"),
		STATUS("rec_status"),
		ERA("rec_era");

		private final String name;

		private Recording(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override public String toString() {
			return getName();
		}
	}



}
