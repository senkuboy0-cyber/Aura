import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
  Alert,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { useNotes } from '../context/NotesContext';
import { COLORS, NOTE_COLORS } from '../constants/theme';
import ColorPicker from '../components/ColorPicker';
import PriorityPicker from '../components/PriorityPicker';
import FolderPicker from '../components/FolderPicker';
import DateTimePicker from '../components/DateTimePicker';
import SubtaskInput from '../components/SubtaskInput';
import LockToggle from '../components/LockToggle';

export default function EditNoteScreen({ route, navigation }) {
  const { note } = route.params;
  const {
    updateNote,
    deleteNote,
    archiveNote,
    togglePin,
    toggleFavorite,
    toggleComplete,
    addSubtask,
    toggleSubtask,
    deleteSubtask,
    lockNote,
    unlockNote,
    verifyPin,
    isPinSet,
  } = useNotes();

  const [title, setTitle] = useState(note.title);
  const [description, setDescription] = useState(note.description);
  const [priority, setPriority] = useState(note.priority);
  const [noteColor, setNoteColor] = useState(note.color);
  const [folder, setFolder] = useState(note.folder);
  const [dueDate, setDueDate] = useState(note.dueDate ? new Date(note.dueDate) : null);
  const [reminder, setReminder] = useState(note.reminder ? new Date(note.reminder) : null);
  const [subtasks, setSubtasks] = useState(note.subtasks || []);
  const [isLocked, setIsLocked] = useState(note.isLocked);
  const [showPinInput, setShowPinInput] = useState(false);
  const [pin, setPin] = useState('');
  const [showOptions, setShowOptions] = useState(false);

  const handleSave = async () => {
    if (!title.trim()) {
      Alert.alert('Error', 'Please enter a title');
      return;
    }

    await updateNote(note.id, {
      title: title.trim(),
      description: description.trim(),
      priority,
      color: noteColor,
      folder,
      dueDate: dueDate?.toISOString() || null,
      reminder: reminder?.toISOString() || null,
      subtasks,
    });

    navigation.goBack();
  };

  const handleDelete = () => {
    Alert.alert(
      'Delete Note',
      'Are you sure you want to delete this note?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete',
          style: 'destructive',
          onPress: async () => {
            await deleteNote(note.id);
            navigation.goBack();
          },
        },
      ]
    );
  };

  const handleArchive = async () => {
    await archiveNote(note.id);
    navigation.goBack();
  };

  const handleLockToggle = async (value) => {
    if (value) {
      Alert.prompt(
        'Set PIN',
        'Enter a 4-digit PIN to lock this note',
        [
          { text: 'Cancel', style: 'cancel' },
          {
            text: 'Lock',
            onPress: async (pinValue) => {
              if (pinValue && pinValue.length === 4) {
                await lockNote(note.id, pinValue);
                setIsLocked(true);
              } else {
                Alert.alert('Error', 'PIN must be 4 digits');
              }
            },
          },
        ],
        'secure-text'
      );
    } else {
      setShowPinInput(true);
    }
  };

  const handleUnlock = async () => {
    if (pin.length !== 4) {
      Alert.alert('Error', 'PIN must be 4 digits');
      return;
    }
    const success = await unlockNote(note.id, pin);
    if (success) {
      setIsLocked(false);
      setShowPinInput(false);
      setPin('');
    } else {
      Alert.alert('Error', 'Invalid PIN');
      setPin('');
    }
  };

  const handleAddSubtask = async () => {
    Alert.prompt(
      'Add Subtask',
      'Enter subtask title',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Add',
          onPress: (text) => {
            if (text && text.trim()) {
              const newSubtask = {
                id: Date.now().toString(),
                title: text.trim(),
                isCompleted: false,
              };
              setSubtasks([...subtasks, newSubtask]);
            }
          },
        },
      ]
    );
  };

  return (
    <SafeAreaView style={styles.container} edges={['bottom']}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={styles.keyboardView}
      >
        <ScrollView
          style={styles.scrollView}
          contentContainerStyle={styles.scrollContent}
          showsVerticalScrollIndicator={false}
        >
          {/* Action Buttons */}
          <View style={styles.actionRow}>
            <TouchableOpacity
              style={[styles.actionButton, note.isPinned && styles.actionButtonActive]}
              onPress={() => togglePin(note.id)}
            >
              <Ionicons
                name={note.isPinned ? 'pin' : 'pin-outline'}
                size={20}
                color={note.isPinned ? COLORS.primary : COLORS.textSecondary}
              />
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.actionButton, note.isFavorite && styles.actionButtonActive]}
              onPress={() => toggleFavorite(note.id)}
            >
              <Ionicons
                name={note.isFavorite ? 'star' : 'star-outline'}
                size={20}
                color={note.isFavorite ? COLORS.warning : COLORS.textSecondary}
              />
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.actionButton, note.isCompleted && styles.actionButtonActive]}
              onPress={() => toggleComplete(note.id)}
            >
              <Ionicons
                name={note.isCompleted ? 'checkmark-circle' : 'checkmark-circle-outline'}
                size={20}
                color={note.isCompleted ? COLORS.success : COLORS.textSecondary}
              />
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.actionButton}
              onPress={() => setShowOptions(!showOptions)}
            >
              <Ionicons name="ellipsis-vertical" size={20} color={COLORS.textSecondary} />
            </TouchableOpacity>
          </View>

          {/* Options Menu */}
          {showOptions && (
            <View style={styles.optionsMenu}>
              <TouchableOpacity style={styles.optionItem} onPress={handleArchive}>
                <Ionicons name="archive-outline" size={20} color={COLORS.text} />
                <Text style={styles.optionText}>Archive</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.optionItem} onPress={handleDelete}>
                <Ionicons name="trash-outline" size={20} color={COLORS.error} />
                <Text style={[styles.optionText, { color: COLORS.error }]}>Delete</Text>
              </TouchableOpacity>
            </View>
          )}

          {/* Lock Status */}
          {note.isLocked && (
            <View style={styles.lockedBanner}>
              <Ionicons name="lock-closed" size={20} color={COLORS.warning} />
              <Text style={styles.lockedText}>This note is locked</Text>
            </View>
          )}

          {/* Title Input */}
          <View style={styles.inputGroup}>
            <TextInput
              style={[styles.titleInput, {
                backgroundColor: NOTE_COLORS.find(c => c.id === noteColor)?.color || COLORS.noteYellow,
                textDecorationLine: note.isCompleted ? 'line-through' : 'none',
              }]}
              placeholder="Enter note title..."
              placeholderTextColor={COLORS.textSecondary}
              value={title}
              onChangeText={setTitle}
              maxLength={100}
              editable={!note.isLocked}
            />
          </View>

          {/* Description Input */}
          <View style={styles.inputGroup}>
            <TextInput
              style={[styles.descriptionInput, {
                backgroundColor: NOTE_COLORS.find(c => c.id === noteColor)?.color || COLORS.noteYellow,
                textDecorationLine: note.isCompleted ? 'line-through' : 'none',
              }]}
              placeholder="Write your note here..."
              placeholderTextColor={COLORS.textSecondary}
              value={description}
              onChangeText={setDescription}
              multiline
              textAlignVertical="top"
              maxLength={5000}
              editable={!note.isLocked}
            />
            <Text style={styles.charCount}>{description.length}/5000</Text>
          </View>

          {/* Priority Picker */}
          <PriorityPicker
            selectedPriority={priority}
            onSelectPriority={setPriority}
            disabled={note.isLocked}
          />

          {/* Color Picker */}
          <ColorPicker
            selectedColor={noteColor}
            onSelectColor={setNoteColor}
            disabled={note.isLocked}
          />

          {/* Folder Picker */}
          <FolderPicker
            folders={DEFAULT_FOLDERS}
            selectedFolder={folder}
            onSelectFolder={setFolder}
            disabled={note.isLocked}
          />

          {/* Due Date Picker */}
          <DateTimePicker
            label="Due Date"
            value={dueDate}
            onChange={setDueDate}
            minDate={new Date()}
            disabled={note.isLocked}
          />

          {/* Reminder Picker */}
          <DateTimePicker
            label="Reminder"
            value={reminder}
            onChange={setReminder}
            minDate={new Date()}
            disabled={note.isLocked}
          />

          {/* Subtasks */}
          <SubtaskInput
            subtasks={subtasks}
            onAddSubtask={(text) => {
              const newSubtask = {
                id: Date.now().toString(),
                title: text,
                isCompleted: false,
              };
              setSubtasks([...subtasks, newSubtask]);
            }}
            onRemoveSubtask={(id) => setSubtasks(subtasks.filter(s => s.id !== id))}
            onToggleSubtask={(id) => setSubtasks(subtasks.map(s => s.id === id ? { ...s, isCompleted: !s.isCompleted } : s))}
            disabled={note.isLocked}
          />

          {/* Lock Toggle */}
          {!note.isLocked && (
            <LockToggle
              isLocked={isLocked}
              onToggle={handleLockToggle}
              pin={pin}
              onPinChange={setPin}
              showPinInput={showPinInput}
              onPinSubmit={handleUnlock}
              unlockMode
            />
          )}

          {/* Metadata */}
          <View style={styles.metadata}>
            <Text style={styles.metadataText}>
              Created: {new Date(note.createdAt).toLocaleDateString()}
            </Text>
            <Text style={styles.metadataText}>
              Updated: {new Date(note.updatedAt).toLocaleDateString()}
            </Text>
          </View>
        </ScrollView>

        {/* Save Button */}
        <View style={styles.footer}>
          <TouchableOpacity style={styles.saveButton} onPress={handleSave}>
            <LinearGradient
              colors={[COLORS.primary, COLORS.primaryLight]}
              start={{ x: 0, y: 0 }}
              end={{ x: 1, y: 0 }}
              style={styles.saveGradient}
            >
              <Ionicons name="checkmark-circle" size={24} color="#fff" />
              <Text style={styles.saveButtonText}>Save Changes</Text>
            </LinearGradient>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

import { DEFAULT_FOLDERS } from '../constants/theme';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  keyboardView: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
  },
  scrollContent: {
    padding: 16,
    paddingBottom: 100,
  },
  actionRow: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    marginBottom: 16,
    gap: 8,
  },
  actionButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: COLORS.card,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: COLORS.border,
  },
  actionButtonActive: {
    backgroundColor: COLORS.primaryLight + '20',
    borderColor: COLORS.primary,
  },
  optionsMenu: {
    backgroundColor: COLORS.card,
    borderRadius: 12,
    padding: 8,
    marginBottom: 16,
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  optionItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    borderRadius: 8,
  },
  optionText: {
    fontSize: 16,
    marginLeft: 12,
    color: COLORS.text,
  },
  lockedBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: COLORS.warning + '20',
    padding: 12,
    borderRadius: 8,
    marginBottom: 16,
  },
  lockedText: {
    fontSize: 14,
    color: COLORS.warning,
    marginLeft: 8,
    fontWeight: '600',
  },
  inputGroup: {
    marginBottom: 20,
  },
  titleInput: {
    fontSize: 18,
    fontWeight: '600',
    padding: 16,
    borderRadius: 12,
    color: COLORS.text,
  },
  descriptionInput: {
    fontSize: 16,
    padding: 16,
    borderRadius: 12,
    minHeight: 150,
    color: COLORS.text,
  },
  charCount: {
    fontSize: 12,
    color: COLORS.textSecondary,
    textAlign: 'right',
    marginTop: 4,
  },
  metadata: {
    marginTop: 24,
    padding: 16,
    backgroundColor: COLORS.card,
    borderRadius: 12,
  },
  metadataText: {
    fontSize: 12,
    color: COLORS.textSecondary,
    marginBottom: 4,
  },
  footer: {
    padding: 16,
    backgroundColor: COLORS.card,
    borderTopWidth: 1,
    borderTopColor: COLORS.border,
  },
  saveButton: {
    borderRadius: 12,
    overflow: 'hidden',
  },
  saveGradient: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 16,
  },
  saveButtonText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#fff',
    marginLeft: 8,
  },
});
