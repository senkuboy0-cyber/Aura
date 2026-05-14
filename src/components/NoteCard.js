import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Alert,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, PRIORITY_LABELS, NOTE_COLORS } from '../constants/theme';
import { useNotes } from '../context/NotesContext';

export default function NoteCard({ note, onPress }) {
  const { toggleComplete, togglePin, toggleFavorite, archiveNote, deleteNote } = useNotes();

  const noteColor = NOTE_COLORS.find((c) => c.id === note.color)?.color || COLORS.noteYellow;
  const priorityColor = COLORS[note.priority];

  const formatDate = (dateString) => {
    if (!dateString) return null;
    const date = new Date(dateString);
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);

    if (date.toDateString() === today.toDateString()) {
      return 'Today';
    } else if (date.toDateString() === tomorrow.toDateString()) {
      return 'Tomorrow';
    } else {
      return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
    }
  };

  const handleLongPress = () => {
    Alert.alert(
      note.title,
      'Choose an action',
      [
        {
          text: note.isPinned ? 'Unpin' : 'Pin',
          onPress: () => togglePin(note.id),
        },
        {
          text: note.isFavorite ? 'Remove from Favorites' : 'Add to Favorites',
          onPress: () => toggleFavorite(note.id),
        },
        {
          text: 'Archive',
          onPress: () => archiveNote(note.id),
        },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: () => {
            Alert.alert(
              'Delete Note',
              'Are you sure?',
              [
                { text: 'Cancel', style: 'cancel' },
                { text: 'Delete', style: 'destructive', onPress: () => deleteNote(note.id) },
              ]
            );
          },
        },
        { text: 'Cancel', style: 'cancel' },
      ]
    );
  };

  return (
    <TouchableOpacity
      style={[styles.card, { backgroundColor: noteColor }]}
      onPress={onPress}
      onLongPress={handleLongPress}
      activeOpacity={0.8}
    >
      {/* Header Row */}
      <View style={styles.header}>
        <TouchableOpacity
          style={styles.checkbox}
          onPress={() => toggleComplete(note.id)}
        >
          <Ionicons
            name={note.isCompleted ? 'checkmark-circle' : 'ellipse-outline'}
            size={24}
            color={note.isCompleted ? COLORS.success : COLORS.textSecondary}
          />
        </TouchableOpacity>

        <View style={styles.badges}>
          {note.isPinned && (
            <View style={styles.badge}>
              <Ionicons name="pin" size={12} color={COLORS.primary} />
            </View>
          )}
          {note.isFavorite && (
            <View style={styles.badge}>
              <Ionicons name="star" size={12} color={COLORS.warning} />
            </View>
          )}
          {note.isLocked && (
            <View style={styles.badge}>
              <Ionicons name="lock-closed" size={12} color={COLORS.textSecondary} />
            </View>
          )}
          <View style={[styles.priorityBadge, { backgroundColor: priorityColor }]}>
            <Text style={styles.priorityText}>{PRIORITY_LABELS[note.priority]}</Text>
          </View>
        </View>
      </View>

      {/* Title */}
      <Text
        style={[
          styles.title,
          note.isCompleted && styles.completedText,
        ]}
        numberOfLines={2}
      >
        {note.title}
      </Text>

      {/* Description */}
      {note.description && (
        <Text
          style={[
            styles.description,
            note.isCompleted && styles.completedText,
          ]}
          numberOfLines={2}
        >
          {note.description}
        </Text>
      )}

      {/* Subtasks Progress */}
      {note.subtasks && note.subtasks.length > 0 && (
        <View style={styles.subtasksInfo}>
          <Ionicons name="checkbox-outline" size={14} color={COLORS.textSecondary} />
          <Text style={styles.subtasksText}>
            {note.subtasks.filter((s) => s.isCompleted).length}/{note.subtasks.length} tasks
          </Text>
        </View>
      )}

      {/* Footer Row */}
      <View style={styles.footer}>
        {note.dueDate && (
          <View style={styles.footerItem}>
            <Ionicons name="calendar-outline" size={14} color={COLORS.textSecondary} />
            <Text style={styles.footerText}>{formatDate(note.dueDate)}</Text>
          </View>
        )}
        {note.folder && (
          <View style={styles.folderBadge}>
            <Text style={styles.folderText}>{note.folder}</Text>
          </View>
        )}
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  card: {
    borderRadius: 16,
    padding: 16,
    marginBottom: 12,
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 4,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  checkbox: {
    padding: 0,
  },
  badges: {
    flexDirection: 'row',
    gap: 6,
  },
  badge: {
    width: 24,
    height: 24,
    borderRadius: 12,
    backgroundColor: 'rgba(255,255,255,0.7)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  priorityBadge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
  },
  priorityText: {
    fontSize: 10,
    fontWeight: 'bold',
    color: '#fff',
    textTransform: 'uppercase',
  },
  title: {
    fontSize: 16,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: 4,
  },
  description: {
    fontSize: 14,
    color: COLORS.textSecondary,
    marginBottom: 8,
  },
  completedText: {
    textDecorationLine: 'line-through',
    opacity: 0.6,
  },
  subtasksInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
    gap: 4,
  },
  subtasksText: {
    fontSize: 12,
    color: COLORS.textSecondary,
  },
  footer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  footerItem: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  footerText: {
    fontSize: 12,
    color: COLORS.textSecondary,
  },
  folderBadge: {
    backgroundColor: 'rgba(255,255,255,0.5)',
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 6,
  },
  folderText: {
    fontSize: 11,
    color: COLORS.textSecondary,
    textTransform: 'capitalize',
  },
});
