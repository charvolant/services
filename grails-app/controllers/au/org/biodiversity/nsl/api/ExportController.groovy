package au.org.biodiversity.nsl.api

import au.org.biodiversity.nsl.ConfigService
import org.grails.plugins.metrics.groovy.Timed


class ExportController implements UnauthenticatedHandler {

    def flatViewService

    def index() {

        [
                exports: [
                        [label: "${ConfigService.nameTreeName} Names as CSV", url: 'namesCsv'],
                        [label: "${ConfigService.classificationTreeName} Taxon as CSV", url: 'taxonCsv']
                ]
        ]
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = [
            index   : ['html'],
            namesCsv: ['json', 'xml', 'html'],
            taxonCsv: ['json', 'xml', 'html'],
    ]

    static allowedMethods = [
            namesCsv: ["GET"],
            taxonCsv: ["GET"],
    ]

    static namespace = "api"

    @Timed()
    namesCsv() {
        try {
            File exportFile = flatViewService.exportNamesToCSV()
            render(file: exportFile, fileName: exportFile.name, contentType: 'text/plain')
        } finally {
            exportFile.delete()

        }
    }

    @Timed()
    taxonCsv() {
        try {
            File exportFile = flatViewService.exportTaxonToCSV()
            render(file: exportFile, fileName: exportFile.name, contentType: 'text/plain')
        } finally {
            exportFile.delete()
        }
    }
}
