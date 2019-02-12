package app;

import attributes.Attribute;
import characters.Character;
import combat.DistanceBased;
import combat.Effect;
import combat.NoTargetException;
import combat.Targetable;
import combat.skills.Skill;
import combat.skills.SkillWithCountDown;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static app.AttackEvaluation.*;
import static app.Combat.printOptionsForCurrentFriendlyCharacter;
import static app.Combat.progressThroughTurnOfFriendlyCharacter;
import static app.Main.CHARACTERS_ALIVE;
import static app.Main.CONSOLE;

public class SkillManagement {


    protected static void evaluateCharacterSkill(Character character) {
        Skill chosenSkill = chooseSkill(character);
        if (chosenSkill instanceof Targetable) {
            evaluateTargetableSkill(chosenSkill, character);
        } else {
            invokeMethod(chosenSkill.getMethod(), chosenSkill, null);
        }
    }

    private static void evaluateTargetableSkill(Skill skill, Character skillUser) {
        if (!(skill instanceof DistanceBased)) {
            invokeMethod(skill.getMethod(), skill,
                    chooseTargetFromCharacters(getCharactersFromSelectedSide(((Targetable) skill).isTargetOnPlayersSide())));
        } else {
            evaluateDistanceBasedSkill(skill, skillUser);
        }
    }

    private static void evaluateDistanceBasedSkill(Skill skill, Character skillUser) {
        try {
            List<Character> charactersOnSelectedSide = getCharactersFromSelectedSide(
                    ((Targetable) skill).isTargetOnPlayersSide());
            List<Character> charactersInSkillsReach = filterReachableCharacters(skillUser, charactersOnSelectedSide,
                    ((DistanceBased) skill).getReach());
            invokeMethod(skill.getMethod(), skill,
                    chooseTargetFromCharacters(charactersInSkillsReach));
        } catch (NoTargetException targetException) {
            System.out.println(MessageFormat.format("\t{0}. Press Enter to get back.", targetException.getMessage()));
            CONSOLE.nextLine();
            printOptionsForCurrentFriendlyCharacter(skillUser);
        }
    }

    private static Skill chooseSkill(Character character) {
        List<Skill> usableSkills = getUsableSkills(character);
        dealWithOutOfSkillsSituation(usableSkills.size(), character);
        printSkills(usableSkills);
        String input = CONSOLE.nextLine().trim();
        return character.getSkills().get((Integer.parseInt(input) - 1));
    }

    public static List<Skill> getUsableSkills(Character character) {
        return character.getSkills().stream()
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

    public static Method findMethod(Class methodOwnerClass, String methodName, Class targetCharacter) {
        try {
            if (targetCharacter == null) {
                return methodOwnerClass.getMethod(methodName);
            } else {
                return methodOwnerClass.getMethod(methodName, targetCharacter);
            }
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException("Method not found" + nsme);
        }
    }

    public static void invokeMethod(Method method, Skill skill, Character target) {
        try {
            if (target == null) {
                method.invoke(skill);
            } else {
                method.invoke(skill, target);
            }
        } catch (InvocationTargetException | IllegalAccessException iae) {
            throw new RuntimeException("Method invoking error" + iae);
        }
    }

    protected static void nullifyEffect(Effect effect, Character player) {
        for (Attribute attribute : player.getAttributes()) {
            if (attribute.getName().equals(effect.getAttribute().getName())) {
                attribute.decrease(effect.getValue());
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
                    invokeMethod(current.getMethodToInvoke(), current, null);
                    iterator.remove();
                }
            }
        }
    }

    protected static void refreshEffects() {
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            Iterator<Effect> iterator = character.getEffects().iterator();
            while (iterator.hasNext()) {
                Effect current = iterator.next();
                current.setDuration(current.getDuration() - 1);
                if (current.getDuration() <= 0) {
                    System.out.println(MessageFormat.format(
                            "\n\tEffect of {0} expired on {1}.\n", current.getName(), character.getName()));
                    nullifyEffect(current, character);
                    iterator.remove();
                }
            }
        }
    }
}
