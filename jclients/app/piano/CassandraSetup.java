package piano;

import services.CassandraHelper;

public class CassandraSetup {

    public static void main(String[] args) {
        CassandraHelper cassandraHelper = new CassandraHelper();
        cassandraHelper.createPianoKeyspace();
    }

}
