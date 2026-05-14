import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, PRIORITIES, PRIORITY_LABELS } from '../constants/theme';

const PRIORITIES_DATA = [
  { id: PRIORITIES.HIGH, icon: 'arrow-up', color: COLORS.high },
  { id: PRIORITIES.MEDIUM, icon: 'remove', color: COLORS.medium },
  { id: PRIORITIES.LOW, icon: 'arrow-down', color: COLORS.low },
];

export default function PriorityPicker({ selectedPriority, onSelectPriority, disabled }) {
  return (
    <View style={styles.container}>
      <Text style={styles.label}>Priority</Text>
      <View style={styles.prioritiesContainer}>
        {PRIORITIES_DATA.map((priority) => (
          <TouchableOpacity
            key={priority.id}
            style={[
              styles.priorityOption,
              { borderColor: priority.color },
              selectedPriority === priority.id && {
                backgroundColor: priority.color,
              },
              disabled && styles.disabled,
            ]}
            onPress={() => !disabled && onSelectPriority(priority.id)}
            disabled={disabled}
          >
            <Ionicons
              name={priority.icon}
              size={18}
              color={selectedPriority === priority.id ? '#fff' : priority.color}
            />
            <Text
              style={[
                styles.priorityText,
                { color: selectedPriority === priority.id ? '#fff' : priority.color },
              ]}
            >
              {PRIORITY_LABELS[priority.id]}
            </Text>
          </TouchableOpacity>
        ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: 12,
  },
  prioritiesContainer: {
    flexDirection: 'row',
    gap: 12,
  },
  priorityOption: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
    borderRadius: 12,
    borderWidth: 2,
    gap: 6,
  },
  priorityText: {
    fontSize: 14,
    fontWeight: '600',
  },
  disabled: {
    opacity: 0.5,
  },
});
