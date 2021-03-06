package javr.util;

import java.util.ArrayList;
import java.util.List;

import javr.core.Wire;

public class IdealWire implements Wire {
	private enum State {
		LOW,
		HIGH,
		RISING,
		FALLING
	}

	private final String label;
	private State state;

	public IdealWire(String name) {
		this.label = name;
		this.state = State.LOW;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public boolean write(boolean state) {
		if (read() != state) {
			// Indicates a change has occurred
			this.state = state ? State.RISING : State.FALLING;
			return true;
		} else {
			// No change has occurred
			return false;
		}
	}

	@Override
	public boolean read() {
		return (state == State.HIGH || state == State.FALLING);
	}

	@Override
	public boolean isRising() {
		return state == State.RISING;
	}

	@Override
	public boolean clock() {
		switch (state) {
		case RISING:
			state = State.HIGH;
			break;
		case FALLING:
			state = State.LOW;
			break;
		default:
			// do nothing
			return false;
		}
		return true;
	}

	@Override
	public void reset() {
		this.state = State.LOW;
	}

	@Override
	public String toString() {
		return state.toString();
	}
}
