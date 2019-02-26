package app;

import attributes.Attribute;
import characters.Character;
import combat.Aerial;
import combat.DistanceBased;
import combat.Targetable;
import combat.effects.AttributeEffect;
import combat.effects.Effect;
import combat.effects.StatusEffect;
import combat.exceptions.NoTargetException;
import combat.skills.Skill;
import combat.skills.SkillWithCountDown;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static app.Main.CHARACTERS_ALIVE;
import static app.Main.CONSOLE;

public class SkillManagement extends Combat {


    protected void evaluateCharacterSkill(Character character) {
        Skill chosenSkill = chooseSkill(character);
        if (chosenSkill instanceof Targetable) {
            evaluateTargetableSkill(chosenSkill, character);
        } else if (chosenSkill instanceof Aerial) {
            evaluateAerialSkill(chosenSkill, character);
        } else {
            chosenSkill.applyTo(null);
        }
    }

    private void evaluateTargetableSkill(Skill skill, Character skillUser) {
        AttackEvaluation attackEvaluation = new AttackEvaluation();
        if (skill instanceof DistanceBased) {
            evaluateDistanceBasedSkill(skill, skillUser);
        } else {
            skill.applyTo(Collections.singletonList(
                    attackEvaluation.chooseTargetFromCharacters(
                            attackEvaluation.getCharactersFromSelectedSide(
                                    ((Targetable) skill).isTargetOnPlayersSide()))));
        }
    }

    private void evaluateDistanceBasedSkill(Skill skill, Character skillUser) {
        try {
            List<Character> charactersInSkillsReach = getFilteredCharacterFromSelectedSide(skill, skillUser);
            skill.applyTo(Collections.singletonList(
                    new AttackEvaluation().chooseTargetFromCharacters(charactersInSkillsReach)));
        } catch (NoTargetException targetException) {
            System.out.println(MessageFormat.format("\t{0}. Press Enter to get back.", targetException.getMessage()));
            CONSOLE.nextLine();
            printOptionsForCurrentFriendlyCharacter(skillUser);
        }
    }

    private void evaluateAerialSkill(Skill skill, Character skillUser) {
        List<Character> charactersInSkillsReach = getFilteredCharacterFromSelectedSide(skill, skillUser);
        skill.applyTo(charactersInSkillsReach);
    }

    private List<Character> getFilteredCharacterFromSelectedSide(Skill skill, Character skillUser) {
        AttackEvaluation attackEvaluation = new AttackEvaluation();
        boolean side = skill instanceof Targetable ?
                ((Targetable) skill).isTargetOnPlayersSide() : ((Aerial) skill).areTargetsOnPlayersSide();
        List<Character> charactersOnSelectedSide = attackEvaluation.getCharactersFromSelectedSide(
                side);
        return attackEvaluation.filterReachableCharacters(skillUser, charactersOnSelectedSide,
                ((DistanceBased) skill).getReach());
    }

    private Skill chooseSkill(Character character) {
        List<Skill> usableSkills = getUsableSkills(character);
        dealWithOutOfSkillsSituation(usableSkills.size(), character);
        printSkills(usableSkills);
        String input = CONSOLE.nextLine().trim();
        return character.getSkills().get((Integer.parseInt(input) - 1));
    }

    protected List<Skill> getUsableSkills(Character character) {
        return character.getSkills().stream()
                .filter(skill -> skill.getUsagePerBattle() > 0).collect(Collectors.toList());
    }

    private void printSkills(List<Skill> skills) {
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

    private void dealWithOutOfSkillsSituation(int usableSkills, Character character) {
        if (usableSkills == 0) {
            System.out.println("\tOut of skills for this battle. Press Enter to get back.");
            CONSOLE.nextLine();
            progressThroughTurnOfFriendlyCharacter(character);
        }
    }

    public void decreaseSkillUsage(Skill skill) {
        if (skill.getUsagePerBattle() > 0)
            skill.setUsagePerBattle(skill.getUsagePerBattle() - 1);
        else {
            System.out.println(MessageFormat.format("\t{0} cannot be used", skill));
        }
    }

    protected void nullifyEffect(Effect effect, Character character) {
        if (effect instanceof AttributeEffect) {
            for (Attribute attribute : character.getAttributes()) {
                if (attribute.getName().equals(((AttributeEffect) effect).getAttribute().getName())) {
                    attribute.decrease(((AttributeEffect) effect).getValue());
                }
            }
        } else if (effect instanceof StatusEffect) {
            character.getStatuses().put(((StatusEffect) effect).getStatusName(),
                    ((StatusEffect) effect).getStatusValue());
        }
    }

    protected void refreshSkillCountdowns() {
        for (Character character : new ArrayList<>(CHARACTERS_ALIVE)) {
            Iterator<SkillWithCountDown> iterator = character.getSkillWithCountDowns().iterator();
            while (iterator.hasNext()) {
                SkillWithCountDown current = iterator.next();
                current.setCountdown(current.getCountdown() - 1);
                if (current.getCountdown() <= 0) {
                    current.activateCountdownSkill();
                    iterator.remove();
                }
            }
        }
    }

    protected void refreshEffects() {
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
