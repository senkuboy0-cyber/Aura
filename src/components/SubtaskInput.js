import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS } from '../constants/theme';

export default function SubtaskInput({ subtasks, onAddSubtask, onRemoveSubtask, onToggleSubtask, disabled }) {
  const [newSubtask, setNewSubtask] = useState('');

  const handleAdd = () => {
    if (newSubtask.trim()) {
      onAddSubtask(newSubtask.trim());
      setNewSubtask('');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.label}>Subtasks ({subtasks.length})</Text>

      {/* Subtasks List */}
      {subtasks.length > 0 && (
        <View style={styles.subtasksList}>
          {subtasks.map((subtask, index) => (
            <View key={subtask.id} style={styles.subtaskItem}>
              <TouchableOpacity
                style={styles.checkbox}
                onPress={() => onToggleSubtask(subtask.id)}
                disabled={disabled}
              >
                <Ionicons
                  name={subtask.isCompleted ? 'checkbox' : 'square-outline'}
                  size={22}
                  color={subtask.isCompleted ? COLORS.success : COLORS.textSecondary}
                />
              </TouchableOpacity>
              <Text
                style={[
                  styles.subtaskText,
                  subtask.isCompleted && styles.completedText,
                ]}
              >
                {subtask.title}
              </Text>
              {!disabled && (
                <TouchableOpacity
                  style={styles.removeButton}
                  onPress={() => onRemoveSubtask(subtask.id)}
                >
                  <Ionicons name="close" size={18} color={COLORS.error} />
                </TouchableOpacity>
              )}
            </View>
          ))}
        </View>
      )}

      {/* Add Subtask Input */}
      {!disabled && (
        <View style={styles.addContainer}>
          <TextInput
            style={styles.input}
            placeholder="Add a subtask..."
            placeholderTextColor={COLORS.textSecondary}
            value={newSubtask}
            onChangeText={setNewSubtask}
            onSubmitEditing={handleAdd}
            returnKeyType="done"
          />
          <TouchableOpacity
            style={[
              styles.addButton,
              !newSubtask.trim() && styles.addButtonDisabled,
            ]}
            onPress={handleAdd}
            disabled={!newSubtask.trim()}
          >
            <Ionicons
              name="add"
              size={24}
              color={newSubtask.trim() ? '#fff' : COLORS.textSecondary}
            />
          </TouchableOpacity>
        </View>
      )}
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
  subtasksList: {
    backgroundColor: COLORS.card,
    borderRadius: 12,
    overflow: 'hidden',
    marginBottom: 12,
  },
  subtaskItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  checkbox: {
    marginRight: 12,
  },
  subtaskText: {
    flex: 1,
    fontSize: 15,
    color: COLORS.text,
  },
  completedText: {
    textDecorationLine: 'line-through',
    color: COLORS.textSecondary,
  },
  removeButton: {
    padding: 4,
  },
  addContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  input: {
    flex: 1,
    backgroundColor: COLORS.card,
    borderRadius: 12,
    padding: 14,
    fontSize: 15,
    color: COLORS.text,
    borderWidth: 1,
    borderColor: COLORS.border,
  },
  addButton: {
    width: 48,
    height: 48,
    borderRadius: 12,
    backgroundColor: COLORS.primary,
    justifyContent: 'center',
    alignItems: 'center',
  },
  addButtonDisabled: {
    backgroundColor: COLORS.border,
  },
});
