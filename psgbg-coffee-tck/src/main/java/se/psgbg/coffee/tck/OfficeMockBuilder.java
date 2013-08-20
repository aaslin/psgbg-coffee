package se.psgbg.coffee.tck;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import se.psgbg.coffee.api.Coffee;
import se.psgbg.coffee.api.CoffeeContainer;
import se.psgbg.coffee.api.CoffeeContainerEmptyException;
import se.psgbg.coffee.api.CoffeeMaker;
import se.psgbg.coffee.api.Filter;
import se.psgbg.coffee.api.Kitchen;
import se.psgbg.coffee.api.NoFilterFoundException;
import se.psgbg.coffee.api.Office;

public class OfficeMockBuilder {

	static class MockedInstanceVariable<T> {

		private T value;

		public MockedInstanceVariable(T value) {
			this.value = value;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
	}

	private int peopleInOffice = 0;
	private int scoopsOfCoffeeInCoffeeContainer = 0;
	private int scoopsOfCoffeeInThirdCupboardFromLeftUnderCounter = 0;
	private int waterLevelInCoffeeMaker = 0;
	private int filtersInKitchen = 0;
	private int filterInCupboardAboveMicrowaveOven = 0;

	public OfficeMockBuilder peopleInOffice(int peopleInOffice) {
		this.peopleInOffice = peopleInOffice;
		return this;
	}

	public OfficeMockBuilder scoopsOfCoffeeInCoffeeContainer(int scoopsOfCoffeeInCoffeeContainer) {
		this.scoopsOfCoffeeInCoffeeContainer = scoopsOfCoffeeInCoffeeContainer;
		return this;
	}

	public OfficeMockBuilder scoopsOfCoffeeInThirdCupboardFromLeftUnderCounter(int scoopsOfCoffeeOnThirdCupboardFromLeftUnderCounter) {
		this.scoopsOfCoffeeInThirdCupboardFromLeftUnderCounter = scoopsOfCoffeeOnThirdCupboardFromLeftUnderCounter;
		return this;
	}

	public Office create() {
		try {
			return createOffice();
		} catch (CoffeeContainerEmptyException e) {
			return null;
		} catch (NoFilterFoundException e) {
			return null;
		}
	}

	public OfficeMockBuilder waterLevel(int waterLevel) {
		waterLevelInCoffeeMaker = waterLevel;
		return this;
	}

	public OfficeMockBuilder filters(int i) {
		filtersInKitchen = i;
		return this;
	}

	public OfficeMockBuilder filtersInCupboard(int i) {
		filterInCupboardAboveMicrowaveOven = i;
		return this;
	}

	private Office createOffice() throws CoffeeContainerEmptyException, NoFilterFoundException {
		Office office = Mockito.mock(Office.class, Mockito.RETURNS_DEEP_STUBS);
		CoffeeMaker coffeeMaker = Mockito.mock(CoffeeMaker.class, Mockito.RETURNS_DEEP_STUBS);
		Kitchen kitchen = Mockito.mock(Kitchen.class);
		CoffeeContainer coffeeContainer = Mockito.mock(CoffeeContainer.class);
		Mockito.when(office.getCoffeeMaker()).thenReturn(coffeeMaker);
		Mockito.when(office.getKitchen()).thenReturn(kitchen);
		Mockito.when(kitchen.getCoffeeContainer()).thenReturn(coffeeContainer);
		Mockito.when(office.getNumberOfPeopleInOffice()).thenReturn(peopleInOffice);

		final MockedInstanceVariable<Integer> scoopsOfCoffee = new MockedInstanceVariable<Integer>(scoopsOfCoffeeInCoffeeContainer);
		Mockito.when(office.getKitchen().getCoffeeContainer().getScoopOfCoffee()).thenAnswer(new Answer<Coffee>() {

			@Override
			public Coffee answer(InvocationOnMock invocation) throws Throwable {
				if (scoopsOfCoffee.getValue() > 0) {
					scoopsOfCoffee.setValue(scoopsOfCoffee.getValue() - 1);
					return Mockito.mock(Coffee.class);
				} else {
					throw new CoffeeContainerEmptyException();
				}
			}
		});

		final MockedInstanceVariable<Integer> scoopsOfCoffeeInCupBoard = new MockedInstanceVariable<Integer>(scoopsOfCoffeeInThirdCupboardFromLeftUnderCounter);
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				scoopsOfCoffee.setValue(scoopsOfCoffee.getValue() + scoopsOfCoffeeInCupBoard.getValue());
				scoopsOfCoffeeInCupBoard.setValue(0);
				return null;
			}
		}).when(kitchen).lookForCoffeeInThirdCupboardFromLeftUnderCounter();

		final MockedInstanceVariable<Integer> waterInCoffeeMaker = new MockedInstanceVariable<Integer>(waterLevelInCoffeeMaker);
		Mockito.when(coffeeMaker.getWaterLevel()).thenAnswer(new Answer<Integer>() {

			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				return waterInCoffeeMaker.getValue();
			}
		});
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				waterInCoffeeMaker.setValue(waterInCoffeeMaker.getValue() + 1);
				return null;
			}
		}).when(coffeeMaker).addWater();

		final MockedInstanceVariable<Integer> filters = new MockedInstanceVariable<Integer>(filtersInKitchen);
		Mockito.when(office.getKitchen().getFilter()).thenAnswer(new Answer<Filter>() {

			@Override
			public Filter answer(InvocationOnMock invocation) throws Throwable {
				if (filters.getValue() > 0) {
					filters.setValue(filters.getValue() - 1);
					return Mockito.mock(Filter.class);
				} else {
					throw new NoFilterFoundException();
				}
			}
		});
		final MockedInstanceVariable<Integer> filtersInCupboard = new MockedInstanceVariable<Integer>(filterInCupboardAboveMicrowaveOven);
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				filters.setValue(filters.getValue() + filtersInCupboard.getValue());
				filtersInCupboard.setValue(0);
				return null;
			}
		}).when(kitchen).lookForFilterInCupboardAboveMicrowaveOven();

		return office;
	}
}