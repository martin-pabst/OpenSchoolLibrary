package de.sp.tools.validation;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Martin on 07.06.2017.
 */
public class ValidatorTest {

    @Test
    public void testValidationArray() {

        ValidationException ex1 = null;

        ValidatorTestArray objectWithArrayAttribute = new ValidatorTestArray();

        try {

            Validator.validate(objectWithArrayAttribute, null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

        ex1 = null;

        try {

            objectWithArrayAttribute.add(new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post"));
            Validator.validate(objectWithArrayAttribute, null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNull(ex1);

    }

    @Test
    public void testValidationList() {

        ValidationException ex1 = null;

        ValidatorTestList list = new ValidatorTestList();

        try {

            Validator.validate(list, null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

        ex1 = null;

        try {

            list.add(new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post"));
            Validator.validate(list, null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNull(ex1);

    }

    @Test
    public void testValidationAllValid() {

        ValidationException ex1 = null;

        try {

            BaseRequestDataTestClass brtc = new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post");
            brtc.validate(null);

            brtc.addToList(new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post"));
            brtc.validate(null);


        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNull(ex1);

    }

    @Test
    public void testIfListElementsValid() {

        ValidationException ex1 = null;

        try {

            BaseRequestDataTestClass brtc = new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post");

            brtc.addToList(new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post"));
            brtc.addToList(new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 200, "post")); // age 0 leads to Exception

            brtc.validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

    }

    @Test
    public void testListElementsMax() {

        ValidationException ex1 = null;

        try {

            BaseRequestDataTestClass brtc = new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post");

            brtc.addToList(new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post"));
            brtc.addToList(new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post"));
            brtc.addToList(new BaseRequestDataTestClass("", "Martin", "xx.yy@zz.de", 10, "post"));

            brtc.validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

    }

    @Test
    public void testValidationStringLength() {

        ValidationException ex1 = null;

        try {

            (new BaseRequestDataTestClass("", "123456789012345678901234567890A", "xx.yy@zz.de", 10, "post")).validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

    }

    @Test
    public void testStringSanitizing() {

        ValidationException ex1 = null;

        try {

            BaseRequestDataTestClass brtc = new BaseRequestDataTestClass("", "<script>test</script>", "xx.yy@zz.de", 10, "post");
            String oldName = brtc.getName();
            brtc.validate(null);
            String newName = brtc.getName();

            Assert.assertNotEquals(oldName, newName);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNull(ex1);


    }

    @Test
    public void testValidationNotEmpty() {

        ValidationException ex1 = null;

        try {

            (new BaseRequestDataTestClass("", "", "xx.yy@zz.de", 10, "post")).validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

    }

    @Test
    public void testRegEx() {

        ValidationException ex1 = null;

        try {

            (new BaseRequestDataTestClass("", "Martin", "yy@zzde", 10, "post")).validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

        Assert.assertEquals(ex1.getMessage(), BaseRequestDataTestClass.message);

    }

    @Test
    public void testAcceptedValues() {

        ValidationException ex1 = null;

        try {

            (new BaseRequestDataTestClass("", "Martin", "yy@zzde", 10, "nonsense")).validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

        Assert.assertEquals(ex1.getMessage(), BaseRequestDataTestClass.message);

    }

    @Test
    public void testMin() {

        ValidationException ex1 = null;

        try {

            (new BaseRequestDataTestClass("", "Martin", "yy@zz.de", -1, "post")).validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

    }

    @Test
    public void testMax() {

        ValidationException ex1 = null;

        try {

            (new BaseRequestDataTestClass("", "Martin", "yy@zz.de", 200, "post")).validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

    }

    @Test
    public void testValidationNotNull() {

        ValidationException ex1 = null;

        try {

            (new BaseRequestDataTestClass(null, "Martin", "xx.yy@zz.de", 10, "post")).validate(null);

        } catch (ValidationException ex) {
            ex1 = ex;
        }

        Assert.assertNotNull(ex1);

    }

}
