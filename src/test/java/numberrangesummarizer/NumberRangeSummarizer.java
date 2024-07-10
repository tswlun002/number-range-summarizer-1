package numberrangesummarizer;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;
import static numberrangesummarizer.utils.Constants.INVALID_INPUT_STRING_MESS;
import  static  numberrangesummarizer.utils.Constants.CONSTRAINS_INPUT_STRING_MESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Lunga Tsewu
 * @date 10 July 2024
 * @refrence to my code I did last year, Link:<a href="https://github.com/tswlun002/number-range-summarizer/blob/main/src/test/java/numberrangesummarizer/NumberRangeSummarizerTest.java">...</a>
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NumberRangeSummarizerTest {
     private    NumberRangeSummarizerImpl numberRangeSummarizer;
    @BeforeEach
    void setUp() {
        numberRangeSummarizer = new NumberRangeSummarizerImpl();
    }

    @AfterEach
    void tearDown() {
        numberRangeSummarizer=null;
    }

    @ParameterizedTest
    @CsvSource(delimiter = ':' ,value = {"0:10,3,6,7,8,12,13,14,15,21,22,23,24,31","1:2,3,74,24,4,42,3,52,2"})
    void TextCollectWithCorrectInput(int index, String input) {
        var list  = numberRangeSummarizer.collect(input).stream().toList();
        var expectedOutput  = index==0? Arrays.asList(3, 6, 7, 8,10, 12, 13, 14, 15, 21, 22, 23, 24, 31):
                Arrays.asList(2,3, 4, 24,42,52,74);
        assertEquals(expectedOutput,list);
    }
    @ParameterizedTest
    @CsvSource(delimiter = ':' ,value = {":"+INVALID_INPUT_STRING_MESS, "  :"+INVALID_INPUT_STRING_MESS})
    void testCollectWithIncorrectInput_blank_and_emptyString(String input, String expectedOutput) {
        var actual  = Assert.assertThrows(RuntimeException.class, ()->numberRangeSummarizer.collect(input)).getMessage();
        assertEquals(expectedOutput, actual);
    }
    @ParameterizedTest
    @CsvSource(delimiter = ':' ,value = {",,,","1,2,3,5,t,6,7,8",",12,3,4,5,6,6,7,^","9,5,7,3,5,2,52,5,?,0,3,5,2,52"})
    void testCollectWithInputStringOfNumbersAndCharacters(String input) {
        var actual  = Assert.assertThrows(RuntimeException.class, ()->numberRangeSummarizer.collect(input)).getMessage();
        assertEquals(CONSTRAINS_INPUT_STRING_MESS, actual);
    }
    @Test
    void testCollectWitNullInput(){

        var actual  = Assert.assertThrows(RuntimeException.class, ()->numberRangeSummarizer.collect(null)).getMessage();
        assertEquals(INVALID_INPUT_STRING_MESS, actual);

    }
    @ParameterizedTest
    @MethodSource("getValidTestCasesRangeAndSummarize")
    void summarizeCollectionValidInput(String input, String expectedOutput) {
        var  collection = numberRangeSummarizer.collect(input);
        assertEquals(expectedOutput, numberRangeSummarizer.summarizeCollection(collection));
    }
    @ParameterizedTest
    @MethodSource("getInValidTestCasesRangeAndSummarize")
    void summarizeCollectionInvalidInput(Collection<Integer> input, String expectedOutput) {
        var actual = Assertions.assertThrows(RuntimeException.class,
                ()->numberRangeSummarizer.summarizeCollection(input)).getMessage();

        assertEquals(expectedOutput,actual );
    }
    public Stream<Arguments> getValidTestCasesRangeAndSummarize(){
        return  Stream.of(
                Arguments.of("1,3,6,7,8,12,13,14,15,21,22,23,24,31","1,3,6-8,12-15,21-24,31"),
                Arguments.of("1,2,3,3", "1-3"),
                Arguments.of("1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,", "1"),
                Arguments.of("1,2,2,2,2,2,2,2,2,2,2,2,1000", "1-2,1000")

        );
    }

    public Stream<Arguments> getInValidTestCasesRangeAndSummarize(){

        return  Stream.of(
                Arguments.of(null,INVALID_INPUT_STRING_MESS),
                Arguments.of(Collections.EMPTY_LIST,INVALID_INPUT_STRING_MESS)
        );
    }


}