package se.psgbg.coffee.tck;

import org.junit.Assert;
import org.junit.Test;

import se.psgbg.coffee.api.Coffee;
import se.psgbg.coffee.api.CoffeeContainerEmptyException;
import se.psgbg.coffee.api.Filter;
import se.psgbg.coffee.api.NoFilterFoundException;
import se.psgbg.coffee.api.Office;

public class OfficeMockBuilderTest {

	@Test
	public void testCoffeeMaker() {
		Office office = new OfficeMockBuilder().waterLevel(0).create();

		Assert.assertEquals(0, office.getCoffeeMaker().getWaterLevel());
		office.getCoffeeMaker().addWater();
		office.getCoffeeMaker().addWater();
		office.getCoffeeMaker().addWater();
		office.getCoffeeMaker().addWater();
		Assert.assertEquals(4, office.getCoffeeMaker().getWaterLevel());
	}

	@Test
	public void testCoffeeContainer() throws CoffeeContainerEmptyException {
		Office office = new OfficeMockBuilder().scoopsOfCoffeeInCoffeeContainer(2).create();

		Coffee coffee = office.getKitchen().getCoffeeContainer().getScoopOfCoffee();
		Assert.assertNotNull(coffee);
		coffee = office.getKitchen().getCoffeeContainer().getScoopOfCoffee();
		Assert.assertNotNull(coffee);
		try {
			coffee = office.getKitchen().getCoffeeContainer().getScoopOfCoffee();
			Assert.fail();
		} catch (CoffeeContainerEmptyException e) {}
	}

	@Test
	public void testCoffeeInCupboard() throws CoffeeContainerEmptyException {
		Office office = new OfficeMockBuilder().scoopsOfCoffeeInCoffeeContainer(0).scoopsOfCoffeeInThirdCupboardFromLeftUnderCounter(2).create();
		office.getKitchen().lookForCoffeeInThirdCupboardFromLeftUnderCounter();

		Coffee coffee = office.getKitchen().getCoffeeContainer().getScoopOfCoffee();
		Assert.assertNotNull(coffee);
		coffee = office.getKitchen().getCoffeeContainer().getScoopOfCoffee();
		Assert.assertNotNull(coffee);
		try {
			coffee = office.getKitchen().getCoffeeContainer().getScoopOfCoffee();
			Assert.fail();
		} catch (CoffeeContainerEmptyException e) {}
	}

	@Test
	public void testPeopleInOffice() {
		Office office = new OfficeMockBuilder().peopleInOffice(3).create();
		Assert.assertEquals(3, office.getNumberOfPeopleInOffice());
	}

	@Test
	public void testFilters() throws NoFilterFoundException {
		Office office = new OfficeMockBuilder().filters(2).create();

		Filter filter = office.getKitchen().getFilter();
		Assert.assertNotNull(filter);
		filter = office.getKitchen().getFilter();
		Assert.assertNotNull(filter);
		try {
			filter = office.getKitchen().getFilter();
			Assert.fail();
		} catch (NoFilterFoundException e) {}
	}

	@Test
	public void testFiltersInCupboard() throws NoFilterFoundException {
		Office office = new OfficeMockBuilder().filters(0).filtersInCupboard(2).create();
		office.getKitchen().lookForFilterInCupboardAboveMicrowaveOven();

		Filter filter = office.getKitchen().getFilter();
		Assert.assertNotNull(filter);
		filter = office.getKitchen().getFilter();
		Assert.assertNotNull(filter);
		try {
			filter = office.getKitchen().getFilter();
			Assert.fail();
		} catch (NoFilterFoundException e) {}
	}
}
