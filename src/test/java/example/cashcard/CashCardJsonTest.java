package example.cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json;

    @Autowired
    private JacksonTester<CashCard[]> jsonList;

    private CashCard[] cashCards;

    @BeforeEach
    void setUp() {
        cashCards = Arrays.array(
                new CashCard(99L, 123.45, "Bob"),
                new CashCard(100L, 1.00, "Bob"),
                new CashCard(101L, 150.00, "Bob"),
                new CashCard(102L, 25.00, "Joe"),
                new CashCard(103L, 5.00, "Joe"));
    }

    @Test
    public void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 123.45, "Bob");
        assertThat(json.write(cashCard)).isStrictlyEqualToJson("singleCashCard.json");
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(123.45);
        assertThat(json.write(cashCard)).hasJsonPathStringValue("@.owner");
        assertThat(json.write(cashCard)).extractingJsonPathStringValue("@.owner")
                .isEqualTo("Bob");
    }

    @Test
    public void cashCardDeserializationTest() throws IOException {
        String expected = """
           {
               "id": 99,
               "amount": 123.45,
               "owner": "Bob"
           }
           """;
        assertThat(json.parse(expected))
                .isEqualTo(new CashCard(99L, 123.45, "Bob"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
        assertThat(json.parseObject(expected).owner()).isEqualTo("Bob");
    }

    @Test
    public void cashCardListSerializationTest() throws IOException {
        assertThat(jsonList.write(cashCards)).isStrictlyEqualToJson("listCashCards.json");
    }

    @Test
    public void cashCardListDeserializationTest() throws IOException {
        String expected= """
                [
                  {"id": 99, "amount": 123.45, "owner": "Bob" },
                  {"id": 100, "amount": 1.00, "owner": "Bob" },
                  {"id": 101, "amount": 150.00, "owner": "Bob" },
                  {"id": 102, "amount":  25.00, "owner": "Joe" },
                  {"id": 103, "amount":  5.00, "owner": "Joe" }
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(cashCards);
    }
}
