package com.seguetech.zippy;

import com.seguetech.zippy.data.model.openfda.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public final class ZippyEvents {
    private ZippyEvents() {
    }

    public static final class SearchResults {
        private final String term;
        private final List<Result> results;

        public SearchResults(String term, List<Result> results) {
            this.term = term;
            this.results = results;
        }

        public String getTerm() {
            return term;
        }

        public List<Result> getResults() {
            return results;
        }
    }

    public static final class Message {
        private final String message;
        private final String producer;

        public Message(String producer, String message) {
            this.producer = producer;
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

        public String getProducer() {
            return this.producer;
        }
    }

    public static final class OpenCabinet {
        private final String cabinet;

        public OpenCabinet(String cabinet) {
            this.cabinet = cabinet;
        }

        public String getCabinet() {
            return this.cabinet;
        }
    }

    public static final class SortChanged {
        private final String sortDirection;
        private final String sortBy;

        public SortChanged(String sortDirection, String sortBy) {
            this.sortDirection = sortDirection;
            this.sortBy = sortBy;
        }

        public String getSortDirection() {
            return this.sortDirection;
        }

        public String getSortBy() {
            return this.sortBy;
        }
    }

    public static final class CabinetRenamed {
        private final String originalName;
        private final String newName;

        public CabinetRenamed(String originalName, String newName) {
            this.originalName = originalName;
            this.newName = newName;
        }

        public String getOriginalName() {
            return this.originalName;
        }

        public String getNewName() {
            return this.newName;
        }
    }

    public static class ImagesFound {
        private final ArrayList<String> urls;

        public ImagesFound(LinkedHashSet<String> urls) {
            this.urls = new ArrayList<>();
            this.urls.addAll(Arrays.asList(urls.toArray(new String[urls.size()])));
        }

        public ArrayList<String> getUrls() {
            return this.urls;
        }
    }
}
