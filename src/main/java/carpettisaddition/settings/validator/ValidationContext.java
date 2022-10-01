package carpettisaddition.settings.validator;

import carpet.settings.ParsedRule;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ValidationContext<T>
{
	@Nullable
	public final ServerCommandSource source;
	public final ParsedRule<T> rule;
	public final T oldValue;
	public final T inputValue;
	public final String valueString;

	public ValidationContext(@Nullable ServerCommandSource source, ParsedRule<T> rule, T inputValue, String valueString)
	{
		this.source = source;
		this.rule = rule;
		this.oldValue = rule.get();
		this.inputValue = inputValue;
		this.valueString = valueString;
	}

	public String ruleName()
	{
		return
				//#if MC >= 11901
				//$$ this.rule.name();
				//#else
				this.rule.name;
		//#endif
	}

	public Optional<ServerCommandSource> optionalSource()
	{
		return Optional.ofNullable(this.source);
	}
}
