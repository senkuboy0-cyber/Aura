import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  Alert,
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { Ionicons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { useNotes } from '../context/NotesContext';
import { COLORS } from '../constants/theme';
import EmptyState from '../components/EmptyState';

export default function ArchiveScreen() {
  const { archivedNotes, unarchiveNote, permanentlyDelete } = useNotes();

  const handleUnarchive = async (note) => {
    Alert.alert(
      'Unarchive Note',
      'This note will be restored to your notes.',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Restore',
          onPress: () => unarchiveNote(note.id),
        },
      ]
    );
  };

  const handlePermanentDelete = (note) => {
    Alert.alert(
      'Permanently Delete',
      'This action cannot be undone. Are you sure?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Delete Forever',
          style: 'destructive',
          onPress: () => permanentlyDelete(note.id),
        },
      ]
    );
  };

  const renderArchivedNote = ({ item }) => (
    <View style={styles.noteCard}>
      <View style={styles.noteContent}>
        <Text style={styles.noteTitle} numberOfLines={1}>
          {item.title}
        </Text>
        {item.description && (
          <Text style={styles.noteDescription} numberOfLines={2}>
            {item.description}
          </Text>
        )}
        <Text style={styles.archivedDate}>
          Archived: {new Date(item.archivedAt).toLocaleDateString()}
        </Text>
      </View>

      <View style={styles.noteActions}>
        <TouchableOpacity
          style={styles.actionButton}
          onPress={() => handleUnarchive(item)}
        >
          <Ionicons name="arrow-undo" size={20} color={COLORS.success} />
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.actionButton}
          onPress={() => handlePermanentDelete(item)}
        >
          <Ionicons name="trash" size={20} color={COLORS.error} />
        </TouchableOpacity>
      </View>
    </View>
  );

  return (
    <SafeAreaView style={styles.container} edges={['top']}>
      <LinearGradient
        colors={[COLORS.primary, COLORS.primaryLight]}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.header}
      >
        <Text style={styles.headerTitle}>Archive</Text>
        <Text style={styles.headerSubtitle}>
          {archivedNotes.length} {archivedNotes.length === 1 ? 'note' : 'notes'}
        </Text>
      </LinearGradient>

      <FlatList
        data={archivedNotes}
        keyExtractor={(item) => item.id}
        renderItem={renderArchivedNote}
        contentContainerStyle={styles.listContent}
        ListEmptyComponent={<EmptyState type="archive" />}
        showsVerticalScrollIndicator={false}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  header: {
    padding: 20,
    paddingTop: 16,
    borderBottomLeftRadius: 24,
    borderBottomRightRadius: 24,
  },
  headerTitle: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#fff',
  },
  headerSubtitle: {
    fontSize: 14,
    color: 'rgba(255,255,255,0.8)',
    marginTop: 4,
  },
  listContent: {
    padding: 16,
    paddingBottom: 100,
  },
  noteCard: {
    backgroundColor: COLORS.card,
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    flexDirection: 'row',
    shadowColor: COLORS.shadow,
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  noteContent: {
    flex: 1,
  },
  noteTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: 4,
  },
  noteDescription: {
    fontSize: 14,
    color: COLORS.textSecondary,
    marginBottom: 8,
  },
  archivedDate: {
    fontSize: 12,
    color: COLORS.textLight,
  },
  noteActions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  actionButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: COLORS.background,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
