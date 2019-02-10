package app;

import attributes.Attribute;
import characters.Character;
import combat.Skill;
import combat.SkillWithCountDown;
import combat.Status;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static app.AttackEvaluation.chooseTargetFromCharacters;
import static app.AttackEvaluation.getCharactersFromSelectedSide;
import static app.Combat.progressThroughTurnOfFriendlyCharacter;
import static app.Main.CHARACTERS_ALIVE;
import static app.Main.CONSOLE;

public class SkillManagement {


    protected static void evaluateCharacterSkill(Character character) {
        Skill chosenSkill = chooseSkill(character);
        if (chosenSkill.getTarget().isTargetable()) {
            invokeMethod(chosenSkill.getMethod(), character,
                    chooseTargetFromCharacters(getCharactersFromSelectedSide(chosenSkill.getTarget().isTargetOnPlayersSide())));
        } else {
            invokeMethod(chosenSkill.getMethod(), character, null);
        }
    }

    private static Skill chooseSkill(Character character) { //maybe it can be merged with chooseTargetFromCharacters
        List<Skill> usableSkills = getUsableSkills(character);
        dealWithOutOfSkillsSituation(usableSkills.size(), character);
        printSkills(usableSkills);
        String input = CONSOLE.nextLine();
        return character.showSpecialAttacks().get((Integer.parseInt(input) - 1));
    }

    public static List<Skill> getUsableSkills(Character character) {
        return character.showSpecialAttacks().stream()
                .filter(skill -> skill.getUsagePerBattle() > 0).collect(Collectors.toList());
    }

    private static void printSkills(List<Skill> skills) {
        System.out.println("\n\tWhich skill do you want to use?");
        for (int i = 1; i <= skills.size(); i++) {
            if (skills.get(i - 1).getUsagePerBattle() > 0) {
                Skill currentSkill = skills.get(i - 1);
                System.out.println(MessageFormat.format("\t{0}. {1} ({2})\n\t\tCan be used {3} more times",
                        i, currentSkill.getName(), currentSkill.getDescription(),
                        currentSkill.getUsagePerBattle()));
            }
        }
    }

    private static void dealWithOutOfSkillsSituation(int usableSkills, Character character) {
        if (usableSkills == 0) {
            System.out.println("\tOut of skills for this battle. Press Enter to get back.");
            CONSOLE.nextLine();
            progressThroughTurnOfFriendlyCharacter(character);
        }
    }

    public static void useSkill(Skill skill) {
        if (skill.getUsagePerBattle() > 0)
            skill.setUsagePerBattle(skill.getUsagePerBattle() - 1);
        else {
            System.out.println(MessageFormat.format("\t{0} cannot be used", skill));
        }
    }

    public static void invokeMethod(Method method, Character character, Character target) {
        try {
            if (target == null) {
                method.invoke(character);
            } else {
                method.invoke(character, target);
            }
        } catch (InvocationTargetException | IllegalAccessException iae) {
            throw new RuntimeException("Method invoking error" + iae);
        }
    }

    protected static void nullifyStatusEffect(Status status, Character player) {
        for (Attribute attribute : player.getAttributes()) {
            if (attribute.getName().equals(status.getAttribute().getName())) {
                attribute.decrease(status.getValue());
            }
        }
    }

    protected static void refreshSkillCountdowns() {
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            Iterator<SkillWithCountDown> iterator = character.getSkillWithCountDowns().iterator();
            while (iterator.hasNext()) {
                SkillWithCountDown current = iterator.next();
                current.setCountdown(current.getCountdown() - 1);
                if (current.getCountdown() <= 0) {
                    invokeMethod(current.getMethodToInvoke(), character, null);
                    iterator.remove();
                }
            }
        }
    }

    protected static void refreshStatuses() {
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            Iterator<Status> iterator = character.getStatuses().iterator();
            while (iterator.hasNext()) {
                Status current = iterator.next();
                current.setDuration(current.getDuration() - 1);
                if (current.getDuration() <= 0) {
                    System.out.println(MessageFormat.format(
                            "\n\tEffect of {0} expired on {1}.\n", current.getName(), character.getName()));
                    nullifyStatusEffect(current, character);
                    iterator.remove();
                }
            }
        }
    }
}
