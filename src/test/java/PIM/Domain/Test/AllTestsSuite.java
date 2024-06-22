package PIM.Domain.Test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Communicator, CRUD, Import and Export")
@SelectClasses({CommunicatorTest.class, CreateTest.class, RetrieveTest.class, RetrieveJSONTest.class, UpdateTest.class, DeleteTest.class, ImportTest.class, ExportTest.class})

public class AllTestsSuite {
}
