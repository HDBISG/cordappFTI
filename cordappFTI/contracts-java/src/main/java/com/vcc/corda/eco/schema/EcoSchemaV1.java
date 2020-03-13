package com.vcc.corda.eco.schema;

import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * An ecoState schema.
 * Caused by: org.h2.jdbc.JdbcSQLDataException: Value too long for column """ECO_CONTENT"" VARCHAR(255)": "STRINGDECODE('<?xml version=\""1.0\"" encoding=\""UTF-8\""?>\n<CertificateOfOrigin>\n\t<ID>ECO201912240601</ID>\n\t<UUID>ECO20191224... (4339)"; SQL statement:
 * insert into eco_states (doc_no, eco_content, fti, linear_id, vcc, output_index, transaction_id) values (?, ?, ?, ?, ?, ?, ?) [22001-199]
 */
public class EcoSchemaV1 extends MappedSchema {
    public EcoSchemaV1() {
        super(EcoSchema.class, 1, ImmutableList.of(PersistentEco.class));
    }

    @Entity
    @Table(name = "eco_states")
    public static class PersistentEco extends PersistentState {

        @Column(name = "fti") private final String fti;
        @Column(name = "vcc") private final String vcc;
        //@Column(name = "eco_content") private final String ecoContent;
        @Column(name = "doc_no") private final String docNo;
        @Column(name = "linear_id") private final UUID linearId;

        public PersistentEco(String fti, String vcc, String docNo,  UUID linearId) {
            this.fti = fti;
            this.vcc = vcc;
         //   this.ecoContent = ecoContent;
            this.docNo = docNo;
            this.linearId = linearId;
        }

        // Default constructor required by hibernate.
        public PersistentEco() {
            this.fti = null;
            this.vcc = null;
        //    this.ecoContent = null;
            this.docNo = null;
            this.linearId = null;
        }

        public String getFti() {
            return fti;
        }

        public String getVcc() {
            return vcc;
        }

/*        public String getEcoContent() {
            return ecoContent;
        }*/

        public String getDocNo() {
            return docNo;
        }

        public UUID getId() {
            return linearId;
        }
    }
}