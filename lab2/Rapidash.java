import ru.ifmo.se.pokemon.*;
public class Rapidash extends Ponyta
{
	public Rapidash(String name, int lvl)
	{
		super(name, lvl);
		setStats(65, 100, 70, 80, 80, 105);
		addMove(new SmartStrike());
	}
}
class SmartStrike extends PhysicalMove
{
	public SmartStrike()
	{
		super(Type.STEEL, 70, 0);
	}
	protected boolean checkAccuracy(Pokemon att, Pokemon def)
	{
		return true;
	}
}