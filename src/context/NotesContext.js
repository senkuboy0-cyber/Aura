import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as SecureStore from 'expo-secure-store';
import * as Crypto from 'expo-crypto';

const NotesContext = createContext();

const NOTES_STORAGE_KEY = '@aura_notes';
const PIN_HASH_KEY = '@aura_pin_hash';

export function NotesProvider({ children }) {
  const [notes, setNotes] = useState([]);
  const [archivedNotes, setArchivedNotes] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [globalPin, setGlobalPin] = useState(null);
  const [isPinSet, setIsPinSet] = useState(false);

  // Load notes from storage
  useEffect(() => {
    loadNotes();
    loadPinStatus();
  }, []);

  const loadNotes = async () => {
    try {
      const storedNotes = await AsyncStorage.getItem(NOTES_STORAGE_KEY);
      if (storedNotes) {
        const { notes: activeNotes, archived } = JSON.parse(storedNotes);
        setNotes(activeNotes || []);
        setArchivedNotes(archived || []);
      }
    } catch (error) {
      console.error('Error loading notes:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const loadPinStatus = async () => {
    try {
      const pinHash = await SecureStore.getItemAsync(PIN_HASH_KEY);
      setIsPinSet(!!pinHash);
      if (pinHash) {
        setGlobalPin(pinHash);
      }
    } catch (error) {
      console.error('Error loading PIN:', error);
    }
  };

  const saveNotes = async (updatedNotes, updatedArchived) => {
    try {
      await AsyncStorage.setItem(
        NOTES_STORAGE_KEY,
        JSON.stringify({
          notes: updatedNotes,
          archived: updatedArchived,
        })
      );
    } catch (error) {
      console.error('Error saving notes:', error);
    }
  };

  // Generate PIN hash
  const hashPin = async (pin) => {
    const hashed = await Crypto.digestStringAsync(
      Crypto.CryptoDigestAlgorithm.SHA256,
      pin
    );
    return hashed;
  };

  // Set Global PIN
  const setGlobalPinCode = async (pin) => {
    try {
      const hash = await hashPin(pin);
      await SecureStore.setItemAsync(PIN_HASH_KEY, hash);
      setGlobalPin(hash);
      setIsPinSet(true);
      return true;
    } catch (error) {
      console.error('Error setting PIN:', error);
      return false;
    }
  };

  // Verify PIN
  const verifyPin = async (pin) => {
    try {
      const hash = await hashPin(pin);
      return hash === globalPin;
    } catch (error) {
      console.error('Error verifying PIN:', error);
      return false;
    }
  };

  // Change PIN
  const changePin = async (oldPin, newPin) => {
    const isValid = await verifyPin(oldPin);
    if (!isValid) return false;
    return await setGlobalPinCode(newPin);
  };

  // Remove PIN
  const removePin = async (pin) => {
    const isValid = await verifyPin(pin);
    if (!isValid) return false;
    try {
      await SecureStore.deleteItemAsync(PIN_HASH_KEY);
      setGlobalPin(null);
      setIsPinSet(false);
      return true;
    } catch (error) {
      console.error('Error removing PIN:', error);
      return false;
    }
  };

  // Add Note
  const addNote = useCallback(async (note) => {
    const newNote = {
      id: Date.now().toString(),
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      isPinned: false,
      isCompleted: false,
      isFavorite: false,
      isLocked: false,
      pin: null,
      subtasks: [],
      color: 'yellow',
      folder: null,
      tags: [],
      reminder: null,
      ...note,
    };
    
    const updatedNotes = [newNote, ...notes];
    setNotes(updatedNotes);
    await saveNotes(updatedNotes, archivedNotes);
    return newNote;
  }, [notes, archivedNotes]);

  // Update Note
  const updateNote = useCallback(async (id, updates) => {
    const updatedNotes = notes.map((note) =>
      note.id === id
        ? { ...note, ...updates, updatedAt: new Date().toISOString() }
        : note
    );
    setNotes(updatedNotes);
    await saveNotes(updatedNotes, archivedNotes);
  }, [notes, archivedNotes]);

  // Delete Note
  const deleteNote = useCallback(async (id) => {
    const updatedNotes = notes.filter((note) => note.id !== id);
    setNotes(updatedNotes);
    await saveNotes(updatedNotes, archivedNotes);
  }, [notes, archivedNotes]);

  // Toggle Pin
  const togglePin = useCallback(async (id) => {
    const note = notes.find((n) => n.id === id);
    if (note) {
      await updateNote(id, { isPinned: !note.isPinned });
    }
  }, [notes, updateNote]);

  // Toggle Favorite
  const toggleFavorite = useCallback(async (id) => {
    const note = notes.find((n) => n.id === id);
    if (note) {
      await updateNote(id, { isFavorite: !note.isFavorite });
    }
  }, [notes, updateNote]);

  // Toggle Complete
  const toggleComplete = useCallback(async (id) => {
    const note = notes.find((n) => n.id === id);
    if (note) {
      await updateNote(id, { isCompleted: !note.isCompleted });
    }
  }, [notes, updateNote]);

  // Archive Note
  const archiveNote = useCallback(async (id) => {
    const note = notes.find((n) => n.id === id);
    if (note) {
      const archivedNote = { ...note, archivedAt: new Date().toISOString() };
      const updatedArchived = [archivedNote, ...archivedNotes];
      const updatedNotes = notes.filter((n) => n.id !== id);
      setNotes(updatedNotes);
      setArchivedNotes(updatedArchived);
      await saveNotes(updatedNotes, updatedArchived);
    }
  }, [notes, archivedNotes]);

  // Unarchive Note
  const unarchiveNote = useCallback(async (id) => {
    const note = archivedNotes.find((n) => n.id === id);
    if (note) {
      const { archivedAt, ...restoredNote } = note;
      const updatedNotes = [restoredNote, ...notes];
      const updatedArchived = archivedNotes.filter((n) => n.id !== id);
      setNotes(updatedNotes);
      setArchivedNotes(updatedArchived);
      await saveNotes(updatedNotes, updatedArchived);
    }
  }, [notes, archivedNotes]);

  // Permanently delete from archive
  const permanentlyDelete = useCallback(async (id) => {
    const updatedArchived = archivedNotes.filter((n) => n.id !== id);
    setArchivedNotes(updatedArchived);
    await saveNotes(notes, updatedArchived);
  }, [notes, archivedNotes]);

  // Lock individual note with PIN
  const lockNote = useCallback(async (id, pin) => {
    const hash = await hashPin(pin);
    await updateNote(id, { isLocked: true, pin: hash });
  }, [updateNote]);

  // Unlock note
  const unlockNote = useCallback(async (id, pin) => {
    const note = notes.find((n) => n.id === id);
    if (note && note.pin) {
      const hash = await hashPin(pin);
      if (hash === note.pin) {
        await updateNote(id, { isLocked: false, pin: null });
        return true;
      }
    }
    return false;
  }, [notes, updateNote]);

  // Add Subtask
  const addSubtask = useCallback(async (noteId, subtaskTitle) => {
    const note = notes.find((n) => n.id === noteId);
    if (note) {
      const newSubtask = {
        id: Date.now().toString(),
        title: subtaskTitle,
        isCompleted: false,
      };
      await updateNote(noteId, {
        subtasks: [...note.subtasks, newSubtask],
      });
    }
  }, [notes, updateNote]);

  // Toggle Subtask
  const toggleSubtask = useCallback(async (noteId, subtaskId) => {
    const note = notes.find((n) => n.id === noteId);
    if (note) {
      const updatedSubtasks = note.subtasks.map((st) =>
        st.id === subtaskId ? { ...st, isCompleted: !st.isCompleted } : st
      );
      await updateNote(noteId, { subtasks: updatedSubtasks });
    }
  }, [notes, updateNote]);

  // Delete Subtask
  const deleteSubtask = useCallback(async (noteId, subtaskId) => {
    const note = notes.find((n) => n.id === noteId);
    if (note) {
      const updatedSubtasks = note.subtasks.filter((st) => st.id !== subtaskId);
      await updateNote(noteId, { subtasks: updatedSubtasks });
    }
  }, [notes, updateNote]);

  // Search notes
  const searchNotes = useCallback((query) => {
    if (!query.trim()) return notes;
    const lowerQuery = query.toLowerCase();
    return notes.filter(
      (note) =>
        note.title.toLowerCase().includes(lowerQuery) ||
        note.description.toLowerCase().includes(lowerQuery)
    );
  }, [notes]);

  // Get sorted notes (pinned first)
  const getSortedNotes = useCallback((filter = 'all') => {
    let filteredNotes = [...notes];
    
    switch (filter) {
      case 'active':
        filteredNotes = filteredNotes.filter((n) => !n.isCompleted);
        break;
      case 'completed':
        filteredNotes = filteredNotes.filter((n) => n.isCompleted);
        break;
      case 'pinned':
        filteredNotes = filteredNotes.filter((n) => n.isPinned);
        break;
      case 'favorites':
        filteredNotes = filteredNotes.filter((n) => n.isFavorite);
        break;
    }
    
    return filteredNotes.sort((a, b) => {
      // Pinned first
      if (a.isPinned && !b.isPinned) return -1;
      if (!a.isPinned && b.isPinned) return 1;
      // Then by updated date
      return new Date(b.updatedAt) - new Date(a.updatedAt);
    });
  }, [notes]);

  const value = {
    notes,
    archivedNotes,
    isLoading,
    isPinSet,
    globalPin,
    addNote,
    updateNote,
    deleteNote,
    togglePin,
    toggleFavorite,
    toggleComplete,
    archiveNote,
    unarchiveNote,
    permanentlyDelete,
    lockNote,
    unlockNote,
    verifyPin,
    setGlobalPinCode,
    changePin,
    removePin,
    addSubtask,
    toggleSubtask,
    deleteSubtask,
    searchNotes,
    getSortedNotes,
  };

  return (
    <NotesContext.Provider value={value}>
      {children}
    </NotesContext.Provider>
  );
}

export function useNotes() {
  const context = useContext(NotesContext);
  if (!context) {
    throw new Error('useNotes must be used within NotesProvider');
  }
  return context;
}
